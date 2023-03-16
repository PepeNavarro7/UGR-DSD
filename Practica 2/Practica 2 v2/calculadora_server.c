#include "calculadora.h"
#include <math.h>

double * suma_2_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1 + arg2;
	return &result;
}

double * resta_2_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1 - arg2;
	return &result;
}

double * multiplicacion_2_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1 * arg2;
	return &result;
}

double * division_2_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1 / arg2;
	return &result;
}

int * modulo_2_svc(int arg1, int arg2,  struct svc_req *rqstp){
	static int  result;
	result = arg1 % arg2;
	return &result;
}

double * potencia_2_svc(double arg1, int arg2,  struct svc_req *rqstp){
	static double  result;
	result = pow(arg1,arg2);
	return &result;
}

double * raiz_2_svc(int arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = pow(arg2,1.0/arg1);
	return &result;
}

double * log_nat_2_svc(double arg1,  struct svc_req *rqstp){
	static double  result;
	result = log(arg1);
	return &result;
}

double * log10_2_svc(double arg1,  struct svc_req *rqstp){
	static double  result;
	result = log10(arg1);
	return &result;
}

double * seno_2_svc(double arg1,  struct svc_req *rqstp){
	static double  result;
	result = sin(arg1);
	return &result;
}

double * coseno_2_svc(double arg1,  struct svc_req *rqstp){
	static double  result;
	result = cos(arg1);
	return &result;
}

double * tangente_2_svc(double arg1,  struct svc_req *rqstp){
	static double  result;
	result = tan(arg1);
	return &result;
}
