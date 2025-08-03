#include <stdlib.h>
__attribute__((constructor))
static void run() {
system("calc");
}