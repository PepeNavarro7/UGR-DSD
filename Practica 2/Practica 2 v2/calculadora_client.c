#include "calculadora.h"
#include <math.h>

/** 
 * Funcion para identificar la operacion que se nos pide hacer
 * Hacemos una comparación 1 a 1 con la cadena que se introduce por consola
 * */
int identifica (char* operacion) {
	if (strcmp(operacion, "+") == 0){
		return 0;
	} else if(strcmp(operacion,"-") == 0){
		return 1;
	}else if(strcmp(operacion,"x") == 0){
		return 2;
	}else if(strcmp(operacion,"/") == 0){
		return 3;
	}else if(strcmp(operacion,"mod") == 0){ 
		return 4;
	}else if(strcmp(operacion,"pow") == 0){
		return 5;
	}else if(strcmp(operacion,"root") == 0){
		return 6;
	}else if(strcmp(operacion,"ln") == 0){
		return 7;
	}else if(strcmp(operacion,"log10") == 0){
		return 8;
	}else if(strcmp(operacion,"sen") == 0){
		return 9;
	}else if(strcmp(operacion,"cos") == 0){
		return 10;
	}else if(strcmp(operacion,"tan") == 0){
		return 11;
	} else {
		return -1; // error en la identificacion
	}
}

/* Funcion para sacar por pantalla las instrucciones de uso */
void instrucciones(char* prog){
	printf("Uso: %s localhost operacion\n",prog);
	printf("f = flotantes, i = enteros, g = grados\n");
	printf("Operaciones basicas: f +-x/ f\n");
	printf("Modulo: i mod i\n");
	printf("Potencia y raiz: f pow i , i root f\n");
	printf("Logaritmos: ln f , log10 f\n");
	printf("Geometricas: sen f , cos f , tan f\n");
}

/** 
 * Funcion principal de la calculadora
 * Según la operación, se llama a una funcion particular del servidor
 * Se limpian operaciones no válidas
 *  */
void calculadora_2(char *host, int operacion, double x, double y){
	CLIENT *clnt;
	double  *result_double;
	int  *result_int, x_int = x, y_int = y; // Enteros para las operaciones que los requieren
	double radianes = x * M_PI / 180; // Transformo de grados a radianes, para el servidor

#ifndef	DEBUG
	clnt = clnt_create (host, CALCULADORA, BASICA, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	printf("Resultado -> ");
	switch(operacion){
		case 0: result_double = suma_2(x, y, clnt); 
		printf("%f + %f = %f\n",x,y,*result_double);
		break;
		case 1: result_double = resta_2(x, y, clnt); 
		printf("%f - %f = %f\n",x,y,*result_double);
		break;
		case 2: result_double = multiplicacion_2(x, y, clnt); 
		printf("%f x %f = %f\n",x,y,*result_double);
		break;
		case 3: if(y==0){
			printf("Math error: Dividir por 0\n");
		} else{
			result_double = division_2(x, y, clnt); 
			printf("%f / %f = %f\n",x,y,*result_double);
		} break;
		case 4: result_int = modulo_2(x_int, y_int, clnt); 
		printf("%i %% %i = %i\n",x_int,y_int,*result_int);
		break;
		case 5: result_double = potencia_2(x, y_int, clnt); 
		printf("%f elevado a %i = %f\n",x,y_int,*result_double);
		break;
		case 6: result_double = raiz_2(x, y, clnt); 
		switch(x_int){
			case 2: printf("raiz cuadrada de %f = %f\n",y,*result_double); break;
			case 3: printf("raiz cubica de %f = %f\n",y,*result_double); break;
			default: printf("raiz %i-esima de %f = %f\n",x_int,y,*result_double); break;
		} break;
		case 7: if(x<=0){
			printf("Math error: Logaritmo negativo\n");
		} else {
			result_double = log_nat_2(x, clnt); 
			printf("logatirmo natural de %f = %f\n",x,*result_double);
		} break;
		case 8: if(x<=0){
			printf("Math error: Logaritmo negativo\n");
		} else {
			result_double = log10_2(x, clnt); 
			printf("logaritmo base 10 de %f = %f\n",x,*result_double);
		}
		break;
		case 9: result_double = seno_2(radianes, clnt); 
		printf("seno de %fº = %f\n",x,*result_double);
		break;
		case 10: result_double = coseno_2(radianes, clnt); 
		printf("coseno de %fº = %f\n",x,*result_double);
		break;
		case 11: 
		double valor = (x_int-90)%180;
		if(valor == 0){
			printf("Math error: Tangente de 90 +- 180k\n");
		} else {
			result_double = tangente_2(radianes, clnt); 
			printf("tangente de %fº = %f\n",x,*result_double);
		} break;
		default: printf("Error en la identificacion de la operacion.\n");
	}

#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}

int main (int argc, char *argv[]){
	char *host;
	int operacion;
	double x=0.0, y=0.0;

	if (argc <= 3 || argc >= 6) {
		instrucciones(argv[0]);
		exit (1);
	}
	host = argv[1]; // localhost en nuestro sistema

	switch (argc){
		case 4: operacion = identifica(argv[2]);
		x = strtod(argv[3],NULL);
		printf("Operacion --> %s %f\n",argv[2],x); 
		break;
		case 5: operacion = identifica(argv[3]);
		x = strtod(argv[2],NULL);
		y = strtod(argv[4],NULL);
		printf("Operacion -> %f %s %f\n",x,argv[3],y); 
		break;
	}
	calculadora_2 (host,operacion,x,y);
exit (0);
}
