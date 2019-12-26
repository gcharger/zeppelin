# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: kernel.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf.internal import enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='kernel.proto',
  package='jupyter',
  syntax='proto3',
  serialized_options=_b('\n-org.apache.zeppelin.interpreter.jupyter.protoB\022JupyterKernelProtoP\001\242\002\rJupyterKernel'),
  serialized_pb=_b('\n\x0ckernel.proto\x12\x07jupyter\"\x1e\n\x0e\x45xecuteRequest\x12\x0c\n\x04\x63ode\x18\x01 \x01(\t\"l\n\x0f\x45xecuteResponse\x12&\n\x06status\x18\x01 \x01(\x0e\x32\x16.jupyter.ExecuteStatus\x12!\n\x04type\x18\x02 \x01(\x0e\x32\x13.jupyter.OutputType\x12\x0e\n\x06output\x18\x03 \x01(\t\"\x0f\n\rCancelRequest\"\x10\n\x0e\x43\x61ncelResponse\"1\n\x11\x43ompletionRequest\x12\x0c\n\x04\x63ode\x18\x01 \x01(\t\x12\x0e\n\x06\x63ursor\x18\x02 \x01(\x05\"%\n\x12\x43ompletionResponse\x12\x0f\n\x07matches\x18\x01 \x03(\t\"\x0f\n\rStatusRequest\"7\n\x0eStatusResponse\x12%\n\x06status\x18\x01 \x01(\x0e\x32\x15.jupyter.KernelStatus\"\r\n\x0bStopRequest\"\x0e\n\x0cStopResponse*\'\n\rExecuteStatus\x12\x0b\n\x07SUCCESS\x10\x00\x12\t\n\x05\x45RROR\x10\x01*)\n\x0cKernelStatus\x12\x0c\n\x08STARTING\x10\x00\x12\x0b\n\x07RUNNING\x10\x01*Q\n\nOutputType\x12\x08\n\x04TEXT\x10\x00\x12\x07\n\x03PNG\x10\x01\x12\x08\n\x04JPEG\x10\x02\x12\x08\n\x04HTML\x10\x03\x12\x07\n\x03SVG\x10\x04\x12\x08\n\x04JSON\x10\x05\x12\t\n\x05LaTeX\x10\x06\x32\xc9\x02\n\rJupyterKernel\x12@\n\x07\x65xecute\x12\x17.jupyter.ExecuteRequest\x1a\x18.jupyter.ExecuteResponse\"\x00\x30\x01\x12\x45\n\x08\x63omplete\x12\x1a.jupyter.CompletionRequest\x1a\x1b.jupyter.CompletionResponse\"\x00\x12;\n\x06\x63\x61ncel\x12\x16.jupyter.CancelRequest\x1a\x17.jupyter.CancelResponse\"\x00\x12;\n\x06status\x12\x16.jupyter.StatusRequest\x1a\x17.jupyter.StatusResponse\"\x00\x12\x35\n\x04stop\x12\x14.jupyter.StopRequest\x1a\x15.jupyter.StopResponse\"\x00\x42U\n-org.apache.zeppelin.interpreter.jupyter.protoB\x12JupyterKernelProtoP\x01\xa2\x02\rJupyterKernelb\x06proto3')
)

_EXECUTESTATUS = _descriptor.EnumDescriptor(
  name='ExecuteStatus',
  full_name='jupyter.ExecuteStatus',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='SUCCESS', index=0, number=0,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ERROR', index=1, number=1,
      serialized_options=None,
      type=None),
  ],
  containing_type=None,
  serialized_options=None,
  serialized_start=397,
  serialized_end=436,
)
_sym_db.RegisterEnumDescriptor(_EXECUTESTATUS)

ExecuteStatus = enum_type_wrapper.EnumTypeWrapper(_EXECUTESTATUS)
_KERNELSTATUS = _descriptor.EnumDescriptor(
  name='KernelStatus',
  full_name='jupyter.KernelStatus',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='STARTING', index=0, number=0,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='RUNNING', index=1, number=1,
      serialized_options=None,
      type=None),
  ],
  containing_type=None,
  serialized_options=None,
  serialized_start=438,
  serialized_end=479,
)
_sym_db.RegisterEnumDescriptor(_KERNELSTATUS)

KernelStatus = enum_type_wrapper.EnumTypeWrapper(_KERNELSTATUS)
_OUTPUTTYPE = _descriptor.EnumDescriptor(
  name='OutputType',
  full_name='jupyter.OutputType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='TEXT', index=0, number=0,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='PNG', index=1, number=1,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='JPEG', index=2, number=2,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='HTML', index=3, number=3,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='SVG', index=4, number=4,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='JSON', index=5, number=5,
      serialized_options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='LaTeX', index=6, number=6,
      serialized_options=None,
      type=None),
  ],
  containing_type=None,
  serialized_options=None,
  serialized_start=481,
  serialized_end=562,
)
_sym_db.RegisterEnumDescriptor(_OUTPUTTYPE)

OutputType = enum_type_wrapper.EnumTypeWrapper(_OUTPUTTYPE)
SUCCESS = 0
ERROR = 1
STARTING = 0
RUNNING = 1
TEXT = 0
PNG = 1
JPEG = 2
HTML = 3
SVG = 4
JSON = 5
LaTeX = 6



_EXECUTEREQUEST = _descriptor.Descriptor(
  name='ExecuteRequest',
  full_name='jupyter.ExecuteRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='code', full_name='jupyter.ExecuteRequest.code', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=25,
  serialized_end=55,
)


_EXECUTERESPONSE = _descriptor.Descriptor(
  name='ExecuteResponse',
  full_name='jupyter.ExecuteResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='status', full_name='jupyter.ExecuteResponse.status', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='type', full_name='jupyter.ExecuteResponse.type', index=1,
      number=2, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='output', full_name='jupyter.ExecuteResponse.output', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=57,
  serialized_end=165,
)


_CANCELREQUEST = _descriptor.Descriptor(
  name='CancelRequest',
  full_name='jupyter.CancelRequest',
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
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=167,
  serialized_end=182,
)


_CANCELRESPONSE = _descriptor.Descriptor(
  name='CancelResponse',
  full_name='jupyter.CancelResponse',
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
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=184,
  serialized_end=200,
)


_COMPLETIONREQUEST = _descriptor.Descriptor(
  name='CompletionRequest',
  full_name='jupyter.CompletionRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='code', full_name='jupyter.CompletionRequest.code', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='cursor', full_name='jupyter.CompletionRequest.cursor', index=1,
      number=2, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=202,
  serialized_end=251,
)


_COMPLETIONRESPONSE = _descriptor.Descriptor(
  name='CompletionResponse',
  full_name='jupyter.CompletionResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='matches', full_name='jupyter.CompletionResponse.matches', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=253,
  serialized_end=290,
)


_STATUSREQUEST = _descriptor.Descriptor(
  name='StatusRequest',
  full_name='jupyter.StatusRequest',
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
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=292,
  serialized_end=307,
)


_STATUSRESPONSE = _descriptor.Descriptor(
  name='StatusResponse',
  full_name='jupyter.StatusResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='status', full_name='jupyter.StatusResponse.status', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=309,
  serialized_end=364,
)


_STOPREQUEST = _descriptor.Descriptor(
  name='StopRequest',
  full_name='jupyter.StopRequest',
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
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=366,
  serialized_end=379,
)


_STOPRESPONSE = _descriptor.Descriptor(
  name='StopResponse',
  full_name='jupyter.StopResponse',
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
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=381,
  serialized_end=395,
)

_EXECUTERESPONSE.fields_by_name['status'].enum_type = _EXECUTESTATUS
_EXECUTERESPONSE.fields_by_name['type'].enum_type = _OUTPUTTYPE
_STATUSRESPONSE.fields_by_name['status'].enum_type = _KERNELSTATUS
DESCRIPTOR.message_types_by_name['ExecuteRequest'] = _EXECUTEREQUEST
DESCRIPTOR.message_types_by_name['ExecuteResponse'] = _EXECUTERESPONSE
DESCRIPTOR.message_types_by_name['CancelRequest'] = _CANCELREQUEST
DESCRIPTOR.message_types_by_name['CancelResponse'] = _CANCELRESPONSE
DESCRIPTOR.message_types_by_name['CompletionRequest'] = _COMPLETIONREQUEST
DESCRIPTOR.message_types_by_name['CompletionResponse'] = _COMPLETIONRESPONSE
DESCRIPTOR.message_types_by_name['StatusRequest'] = _STATUSREQUEST
DESCRIPTOR.message_types_by_name['StatusResponse'] = _STATUSRESPONSE
DESCRIPTOR.message_types_by_name['StopRequest'] = _STOPREQUEST
DESCRIPTOR.message_types_by_name['StopResponse'] = _STOPRESPONSE
DESCRIPTOR.enum_types_by_name['ExecuteStatus'] = _EXECUTESTATUS
DESCRIPTOR.enum_types_by_name['KernelStatus'] = _KERNELSTATUS
DESCRIPTOR.enum_types_by_name['OutputType'] = _OUTPUTTYPE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

ExecuteRequest = _reflection.GeneratedProtocolMessageType('ExecuteRequest', (_message.Message,), dict(
  DESCRIPTOR = _EXECUTEREQUEST,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.ExecuteRequest)
  ))
_sym_db.RegisterMessage(ExecuteRequest)

ExecuteResponse = _reflection.GeneratedProtocolMessageType('ExecuteResponse', (_message.Message,), dict(
  DESCRIPTOR = _EXECUTERESPONSE,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.ExecuteResponse)
  ))
_sym_db.RegisterMessage(ExecuteResponse)

CancelRequest = _reflection.GeneratedProtocolMessageType('CancelRequest', (_message.Message,), dict(
  DESCRIPTOR = _CANCELREQUEST,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.CancelRequest)
  ))
_sym_db.RegisterMessage(CancelRequest)

CancelResponse = _reflection.GeneratedProtocolMessageType('CancelResponse', (_message.Message,), dict(
  DESCRIPTOR = _CANCELRESPONSE,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.CancelResponse)
  ))
_sym_db.RegisterMessage(CancelResponse)

CompletionRequest = _reflection.GeneratedProtocolMessageType('CompletionRequest', (_message.Message,), dict(
  DESCRIPTOR = _COMPLETIONREQUEST,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.CompletionRequest)
  ))
_sym_db.RegisterMessage(CompletionRequest)

CompletionResponse = _reflection.GeneratedProtocolMessageType('CompletionResponse', (_message.Message,), dict(
  DESCRIPTOR = _COMPLETIONRESPONSE,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.CompletionResponse)
  ))
_sym_db.RegisterMessage(CompletionResponse)

StatusRequest = _reflection.GeneratedProtocolMessageType('StatusRequest', (_message.Message,), dict(
  DESCRIPTOR = _STATUSREQUEST,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.StatusRequest)
  ))
_sym_db.RegisterMessage(StatusRequest)

StatusResponse = _reflection.GeneratedProtocolMessageType('StatusResponse', (_message.Message,), dict(
  DESCRIPTOR = _STATUSRESPONSE,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.StatusResponse)
  ))
_sym_db.RegisterMessage(StatusResponse)

StopRequest = _reflection.GeneratedProtocolMessageType('StopRequest', (_message.Message,), dict(
  DESCRIPTOR = _STOPREQUEST,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.StopRequest)
  ))
_sym_db.RegisterMessage(StopRequest)

StopResponse = _reflection.GeneratedProtocolMessageType('StopResponse', (_message.Message,), dict(
  DESCRIPTOR = _STOPRESPONSE,
  __module__ = 'kernel_pb2'
  # @@protoc_insertion_point(class_scope:jupyter.StopResponse)
  ))
_sym_db.RegisterMessage(StopResponse)


DESCRIPTOR._options = None

_JUPYTERKERNEL = _descriptor.ServiceDescriptor(
  name='JupyterKernel',
  full_name='jupyter.JupyterKernel',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  serialized_start=565,
  serialized_end=894,
  methods=[
  _descriptor.MethodDescriptor(
    name='execute',
    full_name='jupyter.JupyterKernel.execute',
    index=0,
    containing_service=None,
    input_type=_EXECUTEREQUEST,
    output_type=_EXECUTERESPONSE,
    serialized_options=None,
  ),
  _descriptor.MethodDescriptor(
    name='complete',
    full_name='jupyter.JupyterKernel.complete',
    index=1,
    containing_service=None,
    input_type=_COMPLETIONREQUEST,
    output_type=_COMPLETIONRESPONSE,
    serialized_options=None,
  ),
  _descriptor.MethodDescriptor(
    name='cancel',
    full_name='jupyter.JupyterKernel.cancel',
    index=2,
    containing_service=None,
    input_type=_CANCELREQUEST,
    output_type=_CANCELRESPONSE,
    serialized_options=None,
  ),
  _descriptor.MethodDescriptor(
    name='status',
    full_name='jupyter.JupyterKernel.status',
    index=3,
    containing_service=None,
    input_type=_STATUSREQUEST,
    output_type=_STATUSRESPONSE,
    serialized_options=None,
  ),
  _descriptor.MethodDescriptor(
    name='stop',
    full_name='jupyter.JupyterKernel.stop',
    index=4,
    containing_service=None,
    input_type=_STOPREQUEST,
    output_type=_STOPRESPONSE,
    serialized_options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_JUPYTERKERNEL)

DESCRIPTOR.services_by_name['JupyterKernel'] = _JUPYTERKERNEL

# @@protoc_insertion_point(module_scope)
