# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: python_interpreter.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='python_interpreter.proto',
  package='',
  syntax='proto3',
  serialized_pb=_b('\n\x18python_interpreter.proto\"5\n\x15\x43odeInterpreteRequest\x12\x0c\n\x04\x63ode\x18\x01 \x01(\t\x12\x0e\n\x06noteId\x18\x02 \x01(\t\"2\n\x10InterpetedResult\x12\x0e\n\x06output\x18\x01 \x01(\t\x12\x0e\n\x06status\x18\x02 \x01(\t\"(\n\x15\x43odeCompletionRequest\x12\x0f\n\x07\x63ontext\x18\x01 \x01(\t\"&\n\x0f\x43odeSuggestions\x12\x13\n\x0bsuggestions\x18\x01 \x03(\t\"%\n\x11ProgressIndicator\x12\x10\n\x08progress\x18\x01 \x01(\x05\"\x06\n\x04Void2\xc7\x01\n\x11PythonInterpreter\x12\x37\n\nInterprete\x12\x16.CodeInterpreteRequest\x1a\x11.InterpetedResult\x12\x38\n\x0c\x41utoComplete\x12\x16.CodeCompletionRequest\x1a\x10.CodeSuggestions\x12%\n\x08Progress\x12\x05.Void\x1a\x12.ProgressIndicator\x12\x18\n\x08Shutdown\x12\x05.Void\x1a\x05.VoidB!\n\x1forg.apache.zeppelin.python2.rpcb\x06proto3')
)
_sym_db.RegisterFileDescriptor(DESCRIPTOR)




_CODEINTERPRETEREQUEST = _descriptor.Descriptor(
  name='CodeInterpreteRequest',
  full_name='CodeInterpreteRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='code', full_name='CodeInterpreteRequest.code', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='noteId', full_name='CodeInterpreteRequest.noteId', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=28,
  serialized_end=81,
)


_INTERPETEDRESULT = _descriptor.Descriptor(
  name='InterpetedResult',
  full_name='InterpetedResult',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='output', full_name='InterpetedResult.output', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='status', full_name='InterpetedResult.status', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=83,
  serialized_end=133,
)


_CODECOMPLETIONREQUEST = _descriptor.Descriptor(
  name='CodeCompletionRequest',
  full_name='CodeCompletionRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='context', full_name='CodeCompletionRequest.context', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=135,
  serialized_end=175,
)


_CODESUGGESTIONS = _descriptor.Descriptor(
  name='CodeSuggestions',
  full_name='CodeSuggestions',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='suggestions', full_name='CodeSuggestions.suggestions', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=177,
  serialized_end=215,
)


_PROGRESSINDICATOR = _descriptor.Descriptor(
  name='ProgressIndicator',
  full_name='ProgressIndicator',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='progress', full_name='ProgressIndicator.progress', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=217,
  serialized_end=254,
)


_VOID = _descriptor.Descriptor(
  name='Void',
  full_name='Void',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=256,
  serialized_end=262,
)

DESCRIPTOR.message_types_by_name['CodeInterpreteRequest'] = _CODEINTERPRETEREQUEST
DESCRIPTOR.message_types_by_name['InterpetedResult'] = _INTERPETEDRESULT
DESCRIPTOR.message_types_by_name['CodeCompletionRequest'] = _CODECOMPLETIONREQUEST
DESCRIPTOR.message_types_by_name['CodeSuggestions'] = _CODESUGGESTIONS
DESCRIPTOR.message_types_by_name['ProgressIndicator'] = _PROGRESSINDICATOR
DESCRIPTOR.message_types_by_name['Void'] = _VOID

CodeInterpreteRequest = _reflection.GeneratedProtocolMessageType('CodeInterpreteRequest', (_message.Message,), dict(
  DESCRIPTOR = _CODEINTERPRETEREQUEST,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:CodeInterpreteRequest)
  ))
_sym_db.RegisterMessage(CodeInterpreteRequest)

InterpetedResult = _reflection.GeneratedProtocolMessageType('InterpetedResult', (_message.Message,), dict(
  DESCRIPTOR = _INTERPETEDRESULT,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:InterpetedResult)
  ))
_sym_db.RegisterMessage(InterpetedResult)

CodeCompletionRequest = _reflection.GeneratedProtocolMessageType('CodeCompletionRequest', (_message.Message,), dict(
  DESCRIPTOR = _CODECOMPLETIONREQUEST,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:CodeCompletionRequest)
  ))
_sym_db.RegisterMessage(CodeCompletionRequest)

CodeSuggestions = _reflection.GeneratedProtocolMessageType('CodeSuggestions', (_message.Message,), dict(
  DESCRIPTOR = _CODESUGGESTIONS,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:CodeSuggestions)
  ))
_sym_db.RegisterMessage(CodeSuggestions)

ProgressIndicator = _reflection.GeneratedProtocolMessageType('ProgressIndicator', (_message.Message,), dict(
  DESCRIPTOR = _PROGRESSINDICATOR,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:ProgressIndicator)
  ))
_sym_db.RegisterMessage(ProgressIndicator)

Void = _reflection.GeneratedProtocolMessageType('Void', (_message.Message,), dict(
  DESCRIPTOR = _VOID,
  __module__ = 'python_interpreter_pb2'
  # @@protoc_insertion_point(class_scope:Void)
  ))
_sym_db.RegisterMessage(Void)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\037org.apache.zeppelin.python2.rpc'))
import grpc
from grpc.beta import implementations as beta_implementations
from grpc.beta import interfaces as beta_interfaces
from grpc.framework.common import cardinality
from grpc.framework.interfaces.face import utilities as face_utilities


class PythonInterpreterStub(object):

  def __init__(self, channel):
    """Constructor.

    Args:
      channel: A grpc.Channel.
    """
    self.Interprete = channel.unary_unary(
        '/PythonInterpreter/Interprete',
        request_serializer=CodeInterpreteRequest.SerializeToString,
        response_deserializer=InterpetedResult.FromString,
        )
    self.AutoComplete = channel.unary_unary(
        '/PythonInterpreter/AutoComplete',
        request_serializer=CodeCompletionRequest.SerializeToString,
        response_deserializer=CodeSuggestions.FromString,
        )
    self.Progress = channel.unary_unary(
        '/PythonInterpreter/Progress',
        request_serializer=Void.SerializeToString,
        response_deserializer=ProgressIndicator.FromString,
        )
    self.Shutdown = channel.unary_unary(
        '/PythonInterpreter/Shutdown',
        request_serializer=Void.SerializeToString,
        response_deserializer=Void.FromString,
        )


class PythonInterpreterServicer(object):

  def Interprete(self, request, context):
    """Blocking RPC to interpreter pice of code


    """
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def AutoComplete(self, request, context):
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def Progress(self, request, context):
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')

  def Shutdown(self, request, context):
    """Terminates this RPC Server python process
    """
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')


def add_PythonInterpreterServicer_to_server(servicer, server):
  rpc_method_handlers = {
      'Interprete': grpc.unary_unary_rpc_method_handler(
          servicer.Interprete,
          request_deserializer=CodeInterpreteRequest.FromString,
          response_serializer=InterpetedResult.SerializeToString,
      ),
      'AutoComplete': grpc.unary_unary_rpc_method_handler(
          servicer.AutoComplete,
          request_deserializer=CodeCompletionRequest.FromString,
          response_serializer=CodeSuggestions.SerializeToString,
      ),
      'Progress': grpc.unary_unary_rpc_method_handler(
          servicer.Progress,
          request_deserializer=Void.FromString,
          response_serializer=ProgressIndicator.SerializeToString,
      ),
      'Shutdown': grpc.unary_unary_rpc_method_handler(
          servicer.Shutdown,
          request_deserializer=Void.FromString,
          response_serializer=Void.SerializeToString,
      ),
  }
  generic_handler = grpc.method_handlers_generic_handler(
      'PythonInterpreter', rpc_method_handlers)
  server.add_generic_rpc_handlers((generic_handler,))


class BetaPythonInterpreterServicer(object):
  def Interprete(self, request, context):
    """Blocking RPC to interpreter pice of code


    """
    context.code(beta_interfaces.StatusCode.UNIMPLEMENTED)
  def AutoComplete(self, request, context):
    context.code(beta_interfaces.StatusCode.UNIMPLEMENTED)
  def Progress(self, request, context):
    context.code(beta_interfaces.StatusCode.UNIMPLEMENTED)
  def Shutdown(self, request, context):
    """Terminates this RPC Server python process
    """
    context.code(beta_interfaces.StatusCode.UNIMPLEMENTED)


class BetaPythonInterpreterStub(object):
  def Interprete(self, request, timeout, metadata=None, with_call=False, protocol_options=None):
    """Blocking RPC to interpreter pice of code


    """
    raise NotImplementedError()
  Interprete.future = None
  def AutoComplete(self, request, timeout, metadata=None, with_call=False, protocol_options=None):
    raise NotImplementedError()
  AutoComplete.future = None
  def Progress(self, request, timeout, metadata=None, with_call=False, protocol_options=None):
    raise NotImplementedError()
  Progress.future = None
  def Shutdown(self, request, timeout, metadata=None, with_call=False, protocol_options=None):
    """Terminates this RPC Server python process
    """
    raise NotImplementedError()
  Shutdown.future = None


def beta_create_PythonInterpreter_server(servicer, pool=None, pool_size=None, default_timeout=None, maximum_timeout=None):
  request_deserializers = {
    ('PythonInterpreter', 'AutoComplete'): CodeCompletionRequest.FromString,
    ('PythonInterpreter', 'Interprete'): CodeInterpreteRequest.FromString,
    ('PythonInterpreter', 'Progress'): Void.FromString,
    ('PythonInterpreter', 'Shutdown'): Void.FromString,
  }
  response_serializers = {
    ('PythonInterpreter', 'AutoComplete'): CodeSuggestions.SerializeToString,
    ('PythonInterpreter', 'Interprete'): InterpetedResult.SerializeToString,
    ('PythonInterpreter', 'Progress'): ProgressIndicator.SerializeToString,
    ('PythonInterpreter', 'Shutdown'): Void.SerializeToString,
  }
  method_implementations = {
    ('PythonInterpreter', 'AutoComplete'): face_utilities.unary_unary_inline(servicer.AutoComplete),
    ('PythonInterpreter', 'Interprete'): face_utilities.unary_unary_inline(servicer.Interprete),
    ('PythonInterpreter', 'Progress'): face_utilities.unary_unary_inline(servicer.Progress),
    ('PythonInterpreter', 'Shutdown'): face_utilities.unary_unary_inline(servicer.Shutdown),
  }
  server_options = beta_implementations.server_options(request_deserializers=request_deserializers, response_serializers=response_serializers, thread_pool=pool, thread_pool_size=pool_size, default_timeout=default_timeout, maximum_timeout=maximum_timeout)
  return beta_implementations.server(method_implementations, options=server_options)


def beta_create_PythonInterpreter_stub(channel, host=None, metadata_transformer=None, pool=None, pool_size=None):
  request_serializers = {
    ('PythonInterpreter', 'AutoComplete'): CodeCompletionRequest.SerializeToString,
    ('PythonInterpreter', 'Interprete'): CodeInterpreteRequest.SerializeToString,
    ('PythonInterpreter', 'Progress'): Void.SerializeToString,
    ('PythonInterpreter', 'Shutdown'): Void.SerializeToString,
  }
  response_deserializers = {
    ('PythonInterpreter', 'AutoComplete'): CodeSuggestions.FromString,
    ('PythonInterpreter', 'Interprete'): InterpetedResult.FromString,
    ('PythonInterpreter', 'Progress'): ProgressIndicator.FromString,
    ('PythonInterpreter', 'Shutdown'): Void.FromString,
  }
  cardinalities = {
    'AutoComplete': cardinality.Cardinality.UNARY_UNARY,
    'Interprete': cardinality.Cardinality.UNARY_UNARY,
    'Progress': cardinality.Cardinality.UNARY_UNARY,
    'Shutdown': cardinality.Cardinality.UNARY_UNARY,
  }
  stub_options = beta_implementations.stub_options(host=host, metadata_transformer=metadata_transformer, request_serializers=request_serializers, response_deserializers=response_deserializers, thread_pool=pool, thread_pool_size=pool_size)
  return beta_implementations.dynamic_stub(channel, 'PythonInterpreter', cardinalities, options=stub_options)
# @@protoc_insertion_point(module_scope)
