service Calculadora{
   void ping(),
   double suma(1:double num1, 2:double num2),
   double resta(1:double num1, 2:double num2),
   double multiplica(1:double num1, 2:double num2),
   double divide(1:double num1, 2:double num2),
   i32 modulo(1:i32 num1, 2:i32 num2),
   double potencia(1:double num1, 2:i32 num2),
   double raiz(1:i32 num1, 2:double num2),
}