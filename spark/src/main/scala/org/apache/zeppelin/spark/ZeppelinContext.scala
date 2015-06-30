/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.spark

import java.io.PrintStream
import java.util

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.{QueryExecutionHelper, Row, SQLContext}
import org.apache.zeppelin.display.Input.ParamOption
import org.apache.zeppelin.display.{AngularObjectWatcher, AngularObject, AngularObjectRegistry, GUI}
import org.apache.zeppelin.interpreter.{InterpreterContextRunner, InterpreterException, InterpreterContext}
import org.apache.zeppelin.spark.dep.DependencyResolver
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConversions.asJavaCollection
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.mutable

object ZeppelinContext{

  implicit def toDisplayRDDFunctions[T <: Product](rdd: RDD[T]): DisplayRDDFunctions[T] = new DisplayRDDFunctions[T](rdd)

  implicit def toDisplayTraversableFunctions[T <: Product](traversable: Traversable[T]): DisplayTraversableFunctions[T] = new DisplayTraversableFunctions[T](traversable)

  /**
   * Display HTML code
   * @param htmlContent unescaped HTML content
   * @return HTML content prefixed by the magic %html
   */
  def html(htmlContent: String = "") = s"%html $htmlContent"

  /**
   * Display image using base 64 content
   * @param base64Content base64 content
   * @return base64Content prefixed by the magic %img
   */
  def img64(base64Content: String = "") = s"%img $base64Content"

  /**
   * Display image using URL
   * @param url image URL
   * @return a HTML &lt;img&gt; tag with src = base64 content
   */
  def img(url: String) = s"<img src='$url' />"

  def showRDD(sc: SparkContext, interpreterContext: InterpreterContext, rdd: AnyRef, maxResult: Int): String = {
    sc.setJobGroup("zeppelin-" + interpreterContext.getParagraphId, "Zeppelin", false)
    val queryExecutionHelper: QueryExecutionHelper = new QueryExecutionHelper(sc)

    try {
      val rows:Array[Row] = rdd.getClass().getMethod("take", classOf[Int]).invoke(rdd, new Integer(maxResult+1)).asInstanceOf[Array[Row]]
      val attributes: Seq[Attribute] = queryExecutionHelper.schemaAttributes(rdd)
      val msg = new StringBuilder("")
      try {
        val headerCount = attributes.size
        msg.append("%table ").append(attributes.mkString("","\t","\n"))

        rows.foreach( row => {
          val tableRow: String = (0 until headerCount).map(index =>
            if (row.isNullAt(index)) "null" else row(index).toString
          ).mkString("", "\t", "\n")
          msg.append(tableRow)
        })

        if (rows.length > maxResult) {
          msg.append(s"""\n<font color="red">Results are limited by $maxResult.</font>""")
        }
      } catch {
        case e:Throwable => {
          sc.clearJobGroup()
          throw new InterpreterException(e)
        }
      }
      msg.toString
    } catch{
      case e:Throwable => {
        sc.clearJobGroup()
        throw new InterpreterException(e)
      }
    }
  }
}

/**
 * Spark context for zeppelin.
 *
 */
class ZeppelinContext(private val sc: SparkContext,
                          private val sql: SQLContext,
                          private var interpreterContext: InterpreterContext,
                          private val dep: DependencyResolver,
                          private val out: PrintStream,
                          private var maxResult: Int)  extends mutable.HashMap[String,Any] {

  val logger:Logger = LoggerFactory.getLogger(classOf[ZeppelinContext])

  private var gui: GUI = null

  /**
   * Load dependency for interpreter and runtime (driver).
   * And distribute them to spark cluster (sc.add())
   *
   * @param artifact "group:artifact:version" or file path like "/somepath/your.jar"
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def load(artifact: String): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, true))
  }

  /**
   * Load dependency and it's transitive dependencies for interpreter and runtime (driver).
   * And distribute them to spark cluster (sc.add())
   *
   * @param artifact "groupId:artifactId:version" or file path like "/somepath/your.jar"
   * @param excludes exclusion list of transitive dependency. list of "groupId:artifactId" string.
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def load(artifact: String, excludes: Iterable[String]): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, asJavaCollection(excludes), true))
  }

  /**
   * Load dependency and it's transitive dependencies for interpreter and runtime (driver).
   * And distribute them to spark cluster (sc.add())
   *
   * @param artifact "groupId:artifactId:version" or file path like "/somepath/your.jar"
   * @param excludes exclusion list of transitive dependency. list of "groupId:artifactId" string.
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def load(artifact: String, excludes: util.Collection[String]): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, excludes, true))
  }

  /**
   * Load dependency for interpreter and runtime, and then add to sparkContext.
   * But not adding them to spark cluster
   *
   * @param artifact "groupId:artifactId:version" or file path like "/somepath/your.jar"
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def loadLocal(artifact: String): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, false))
  }

  /**
   * Load dependency and it's transitive dependencies and then add to sparkContext.
   * But not adding them to spark cluster
   *
   * @param artifact "groupId:artifactId:version" or file path like "/somepath/your.jar"
   * @param excludes exclusion list of transitive dependency. list of "groupId:artifactId" string.
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def loadLocal(artifact: String, excludes: Iterable[String]): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, asJavaCollection(excludes), false))
  }

  /**
   * Load dependency and it's transitive dependencies and then add to sparkContext.
   * But not adding them to spark cluster
   *
   * @param artifact "groupId:artifactId:version" or file path like "/somepath/your.jar"
   * @param excludes exclusion list of transitive dependency. list of "groupId:artifactId" string.
   * @return
   * @throws Exception
   */
  @throws(classOf[Exception])
  def loadLocal(artifact: String, excludes: util.Collection[String]): Iterable[String] = {
    return collectionAsScalaIterable(dep.load(artifact, excludes, false))
  }

  /**
   * Add maven repository
   *
   * @param id id of repository ex) oss, local, snapshot
   * @param url url of repository. supported protocol : file, http, https
   */
  def addRepo(id: String, url: String) {
    addRepo(id, url, false)
  }

  /**
   * Add maven repository
   *
   * @param id id of repository
   * @param url url of repository. supported protocol : file, http, https
   * @param snapshot true if it is snapshot repository
   */
  def addRepo(id: String, url: String, snapshot: Boolean) {
    dep.addRepo(id, url, snapshot)
  }

  /**
   * Remove maven repository by id
   * @param id id of repository
   */
  def removeRepo(id: String) {
    dep.delRepo(id)
  }

  /**
   * Define and retrieve the current value of an input from the GUI
   * @param name name of the GUI input
   * @return current input value
   */
  def input (name: String): Any = {
    input(name, "")
  }

  /**
   * Define a default value and retrieve the current value of an input from the GUI
   * @param name name of the GUI input
   * @param defaultValue default value for the input
   * @return current input value
   */
  def input(name: String, defaultValue: Any): Any = {
    gui.input(name, defaultValue)
  }

  /**
   * Define an HMTL &lt;select&gt; and retrieve the current value from the GUI
   * @param name name of the HMTL &lt;select&gt;
   * @param options list of (value,displayedValue)
   * @return current selected element
   */
  def select(name:String, options: Iterable[(Any, String)]):Any = {
    select(name, "", options)
  }

  /**
   * Define an HMTL &lt;select&gt; with a default value and retrieve the current value from the GUI
   * @param name name of the HMTL &lt;select&gt;
   * @param defaultValue default selected value
   * @param options list of (value,displayedValue)
   * @return current selected element
   */
  def select(name: String, defaultValue: Any, options: Iterable[(Any, String)]): Any = {
    val paramOptions = options.map{case(value,label) => new ParamOption(value,label)}.toArray
    gui.select(name, defaultValue, paramOptions)
  }

  def setGui(o: GUI) {
    this.gui = o
  }

  def getInterpreterContext: InterpreterContext = interpreterContext

  def setInterpreterContext(interpreterContext: InterpreterContext):Unit = {
    this.interpreterContext = interpreterContext
  }

  /**
   * Set max result for display
   * @param maxResult max result for display
   */
  def setMaxResult(maxResult: Int):Unit = {
    this.maxResult = maxResult
  }

  /**
   * Show SchemaRDD or DataFrame
   * @param rdd SchemaRDD or DataFrame object
   */
  def show(rdd: AnyRef):Unit = {
    show(rdd, maxResult)
  }

  /**
   * show SchemaRDD or DataFrame
   * @param rdd SchemaRDD or DataFrame object
   * @param maxResult maximum number of rows to display
   */
  def show(rdd: AnyRef, maxResult: Int) {
    validateRDD(rdd)
    out.print(ZeppelinContext.showRDD(sc, interpreterContext, rdd, maxResult))
  }

  /**
   * Run paragraph by id
   * @param id paragraph id
   */
  def run(id: String):Unit = {
    run(id, this.interpreterContext)
  }

  /**
   * Run paragraph by id
   * @param id paragraph id
   * @param context interpreter context
   */
  def run(id: String, context: InterpreterContext):Unit = {
    if (id == context.getParagraphId) {
      throw new InterpreterException("Can not run current Paragraph")
    }

    context.getRunners.filter(r => r.getParagraphId == id).foreach(r => {
      r.run
      return
    })
    throw new InterpreterException("Paragraph " + id + " not found")
  }

  /**
   * Run paragraph at idx
   * @param idx paragraph index to run
   */
  def run(idx: Int):Unit = {
    run(idx, this.interpreterContext)
  }

  /**
   * Run paragraph at index
   * @param idx index starting from 0
   * @param context interpreter context
   */
  def run(idx: Int, context: InterpreterContext):Unit = {
    val runners: util.List[InterpreterContextRunner] = context.getRunners
    if (idx >= runners.size) {
      throw new InterpreterException("Index out of bound")
    }
    val runner: InterpreterContextRunner = runners.get(idx)
    if (runner.getParagraphId == context.getParagraphId) {
      throw new InterpreterException("Can not run current Paragraph")
    }
    runner.run
  }

  /**
   * Run paragraphs using their index or id
   * @param paragraphIdOrIdx list of paragraph id or idx
   */
  def run(paragraphIdOrIdx: List[Any]):Unit = {
    run(paragraphIdOrIdx, this.interpreterContext)
  }

  /**
   * Run paragraphs using their index or id
   * @param paragraphIdOrIdx list of paragraph id or idx
   */
  def run(paragraphIdOrIdx: List[Any], context: InterpreterContext):Unit = {

    val unknownIds: List[Any] = paragraphIdOrIdx.filter(x => (!x.isInstanceOf[String] && !x.isInstanceOf[Int]))
    if (unknownIds.size > 0) {
      throw new InterpreterException(s"""Paragraphs ${unknownIds.mkString(",")} not found""")
    }
    paragraphIdOrIdx
      .filter(_.isInstanceOf[String])
      .foreach(id => run(id.asInstanceOf[String], context))

    paragraphIdOrIdx
      .filter(_.isInstanceOf[Int])
      .foreach(index => run(index.asInstanceOf[Int], context))

  }

  /**
   * Run all paragraphs, except the current
   */
  def runAll:Unit = {
    runAll(interpreterContext)
  }

  /**
   * Run all paragraphs. except the current
   */
  def runAll(context: InterpreterContext):Unit = {
    context.getRunners.foreach(r => {
      if (r.getParagraphId != context.getParagraphId) r.run
    })
  }

  /**
   * List all paragraps
   * @return paragraph ids as list
   */
  def listParagraphs: List[String] = {
    interpreterContext.getRunners.map(_.getParagraphId).toList
  }

  /**
   * Retrieve an Angular variable by name
   * @param name variable name
   * @return variable value
   */
  def angular(name: String): Any = {
    val ao: AngularObject[_] = interpreterContext.getAngularObjectRegistry.get(name)
    if (ao == null) null else ao.get
  }

  /**
   * Bind value to an Angular variable
   * @param name variable name
   * @param value variable value
   */
  def angularBind(name: String, value: Any):Unit = {
    val registry: AngularObjectRegistry = interpreterContext.getAngularObjectRegistry
    val obj: AngularObject[Any] = registry.get(name).asInstanceOf[AngularObject[Any]]
    if (obj == null) registry.add(name, value) else obj.set(value)
  }

  /**
   * Bind value to an Angular variable with a watcher
   * @param name variable name
   * @param value variable value
   * @param watcher variable watcher
   */
  def angularBind(name: String, value: Any, watcher: AngularObjectWatcher) {
    angularBind(name,value)
    angularWatch(name, watcher)
  }

  /**
   * Unbind an Angular variable
   * @param name variable name
   */
  def angularUnbind(name: String) {
    interpreterContext.getAngularObjectRegistry.remove(name)
  }

  /**
   * Add a watcher to an Angular variable
   * @param name variable name
   * @param watcher watcher
   */
  def angularWatch(name: String, watcher: AngularObjectWatcher):Unit = {
    val registry: AngularObjectRegistry = interpreterContext.getAngularObjectRegistry
    val obj: AngularObject[_] = registry.get(name)
    if (obj != null) obj.addWatcher(watcher)
  }

  /**
   * Add a watcher function to an Angular variable
   * @param name variable name
   * @param func watcher function
   */
  def angularWatch(name: String, func: (Any, Any) => Unit):Unit = {
    val w: AngularObjectWatcher = new AngularObjectWatcher((getInterpreterContext)) {
      override def watch(oldObject: Any, newObject: Any, context: InterpreterContext) {
        func.apply(newObject, newObject)
      }
    }
    angularWatch(name, w)
  }

  /**
   * Add a watcher function with an arbitrary interpreter to an Angular variable
   * @param name variable name
   * @param func watcher function with an arbitrary interpreter
   */
  def angularWatch(name: String, func: (Any, Any, InterpreterContext) => Unit):Unit = {
    val w: AngularObjectWatcher = new AngularObjectWatcher((getInterpreterContext)) {
      override def watch(oldObject: Any, newObject: Any, context: InterpreterContext) {
        func.apply(oldObject, newObject, context)
      }
    }
    angularWatch(name, w)
  }

  /**
   * Stop watching an Angular variable with given watcher
   * @param name variable name
   * @param watcher watcher
   */
  def angularUnwatch(name: String, watcher: AngularObjectWatcher) {
    val registry: AngularObjectRegistry = interpreterContext.getAngularObjectRegistry
    val obj: AngularObject[_] = registry.get(name)
    if (obj != null) {
      obj.removeWatcher(watcher)
    }
  }

  /**
   * Stop watching an Angular variable and remove all watchers
   * @param name variable name
   */
  def angularUnwatch(name: String) {
    val registry: AngularObjectRegistry = interpreterContext.getAngularObjectRegistry
    val obj: AngularObject[_] = registry.get(name)
    if (obj != null) {
      obj.clearAllWatchers
    }
  }

  /**
   * Display rdd as table using default 'spark.max.result' to restrict the results
   * @param rdd rdd
   * @param columnsLabel varargs of columns label
   * @tparam T rdd type (should be either a case class or a tuple)
   */
  def display[T<: Product](rdd: RDD[T], columnsLabel: String*):Unit = {
    new DisplayRDDFunctions[T](rdd).display(columnsLabel: _*)(new SparkMaxResult(this.maxResult))
  }

  /**
   * Display rdd as table using the passed maxResult to restrict the results
   * @param rdd rdd
   * @param maxResult restrict the rdd to maxResult
   * @param columnsLabel varargs of columns label
   * @tparam T rdd type (should be either a case class or a tuple)
   */
  def display[T<: Product](rdd: RDD[T], maxResult:Int, columnsLabel: String*):Unit = {
    new DisplayRDDFunctions[T](rdd).display(columnsLabel: _*)(new SparkMaxResult(maxResult))
  }

  /**
   * Display a Scala traversable as table
   * @param traversable Scala traversable
   * @param columnsLabel varargs of columns label
   * @tparam T rdd type (should be either a case class or a tuple)
   */
  def display[T<: Product](traversable: Traversable[T], columnsLabel: String*):Unit = {
    new DisplayTraversableFunctions[T](traversable).display(columnsLabel: _*)
  }

  /**
   * Display HTML code
   * @param htmlContent unescaped HTML content
   * @return HTML content prefixed by the magic %html
   */
  def html(htmlContent: String = "") = ZeppelinContext.html(htmlContent)

  /**
   * Display image using base 64 content
   * @param base64Content base64 content
   * @return base64Content prefixed by the magic %img
   */
  def img64(base64Content: String = "") = ZeppelinContext.img64(base64Content)

  /**
   * Display image using URL
   * @param url image URL
   * @return a HTML &lt;img&gt; tag with src = base64 content
   */
  def img(url: String) = ZeppelinContext.img(url)

  private def validateRDD(rdd: AnyRef): Unit = {
    var cls: Class[_]  = null
    try {
      cls = Class.forName("org.apache.spark.sql.DataFrame")
    } catch {
      case cnfe: ClassNotFoundException => {}
      case e:Throwable => throw new InterpreterException(e)
    }

    if (cls == null) {
      try {
        cls = Class.forName("org.apache.spark.sql.SchemaRDD")
      }catch {
        case cnfe: ClassNotFoundException => {}
        case e:Throwable => throw new InterpreterException(e)
      }
    }

    if (cls == null) {
      throw new InterpreterException("Can not road DataFrame/SchemaRDD class")
    }

    if (cls.isInstance(rdd)) {
      out.print(ZeppelinContext.showRDD(sc, interpreterContext, rdd, maxResult))
    } else {
      out.print(rdd.toString());
    }
  }
}

trait DisplayCollection[T <: Product] {

  def printFormattedData(traversable: Traversable[T], columnLabels: String*): Unit = {
    val providedLabelCount: Int = columnLabels.size
    var maxColumnCount:Int = 1
    val headers = new StringBuilder("%table ")

    val data = new StringBuilder("")

    traversable.foreach(tuple => {
      maxColumnCount = math.max(maxColumnCount,tuple.productArity)
      data.append(tuple.productIterator.mkString("\t")).append("\n")
    })

    if (providedLabelCount > maxColumnCount) {
      headers.append(columnLabels.take(maxColumnCount).mkString("\t")).append("\n")
    } else if (providedLabelCount < maxColumnCount) {
      val missingColumnHeaders = ((providedLabelCount+1) to maxColumnCount).foldLeft[String](""){
        (stringAccumulator,index) =>  if (index==1) s"Column$index" else s"$stringAccumulator\tColumn$index"
      }

      headers.append(columnLabels.mkString("\t")).append(missingColumnHeaders).append("\n")
    } else {
      headers.append(columnLabels.mkString("\t")).append("\n")
    }

    headers.append(data)

    print(headers.toString)
  }
}

class DisplayRDDFunctions[T <: Product] (val rdd: RDD[T]) extends DisplayCollection[T] {

  def display(columnLabels: String*)(implicit sparkMaxResult: SparkMaxResult): Unit = {
    printFormattedData(rdd.take(sparkMaxResult.maxResult), columnLabels: _*)
  }

  def display(sparkMaxResult:Int, columnLabels: String*): Unit = {
    printFormattedData(rdd.take(sparkMaxResult), columnLabels: _*)
  }
}

class DisplayTraversableFunctions[T <: Product] (val traversable: Traversable[T]) extends DisplayCollection[T] {

  def display(columnLabels: String*): Unit = {
    printFormattedData(traversable, columnLabels: _*)
  }
}

class SparkMaxResult(val maxResult: Int) extends Serializable
