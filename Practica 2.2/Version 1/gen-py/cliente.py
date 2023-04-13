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

#print("hacemos ping al server")
client.ping()

print("Introduce la operacion:")
teclado = input()
teclado = teclado.split()
x = float(teclado[0])
y = float(teclado[2])
op = teclado[1]

#En python3 introdujeron el match-case a modo de switch-case
match op:
    case '+': 
        resultado = client.suma(x,y)
    case '-': 
        resultado = client.resta(x,y)
    case 'x': 
        resultado = client.multiplica(x,y)
    case '/': 
        resultado = client.divide(x,y)
print(resultado)

transport.close()
