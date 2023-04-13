from calculadora import Calculadora

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

print("hacemos ping al server")
client.ping()

resultado = client.multiplica(1, 2)
print("1 * 2 = " + str(resultado))
resultado = client.divide(3, 4)
print("3 / 4 = " + str(resultado))

transport.close()
