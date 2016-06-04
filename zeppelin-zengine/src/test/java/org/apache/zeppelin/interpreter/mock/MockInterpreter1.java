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

package org.apache.zeppelin.interpreter.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.thrift.RemoteInterpreterProgress;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;

public class MockInterpreter1 extends Interpreter{
  Map<String, Object> vars = new HashMap<String, Object>();

	public MockInterpreter1(Properties property) {
		super(property);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public InterpreterResult interpret(String st, InterpreterContext context) {
		InterpreterResult result;

		if ("getId".equals(st)) {
			// get unique id of this interpreter instance
			result = new InterpreterResult(InterpreterResult.Code.SUCCESS, "" + this.hashCode());
		} else {
			result = new InterpreterResult(InterpreterResult.Code.SUCCESS, "repl1: " + st);
		}

		if (context.getResourcePool() != null) {
			context.getResourcePool().put(context.getNoteId(), context.getParagraphId(), "result", result);
		}

		return result;
	}

	@Override
	public void cancel(InterpreterContext context) {
	}

	@Override
	public FormType getFormType() {
		return FormType.SIMPLE;
	}

	@Override
	public RemoteInterpreterProgress getProgress(InterpreterContext context) {
		return new RemoteInterpreterProgress();
	}

	@Override
	public Scheduler getScheduler() {
		return SchedulerFactory.singleton().createOrGetFIFOScheduler("test_"+this.hashCode());
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}
}
