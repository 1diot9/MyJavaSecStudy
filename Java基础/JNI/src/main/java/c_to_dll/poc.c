#include <stdlib.h>
#include <stdio.h>
#include <string.h>

//加载进内存前执行
__attribute__ ((__constructor__)) void preload (void){
    system("calc");
}