转载自 https://www.itbaima.cn/document/jviyz2hsht9ete5k


![image-20220918121719900](https://s2.loli.net/2022/09/18/UsqxV8ndNzYmGjy.png)

# 面向对象基础篇

我们在前面已经学习了面向过程编程，也可以自行编写出简单的程序了。我们接着就需要认识 面向对象程序设计（Object Oriented Programming）它是我们在Java语言中要学习的重要内容，面向对象也是高级语言的一大重要特性。

> 面向对象是新手成长的一道分水岭，有的人秒懂，有的人直到最后都无法理解。

这一章开始难度就上来了，所以说请各位小伙伴一定认真。

## 类与对象

类的概念我们在生活中其实已经听说过很多了。

人类、鸟类、鱼类... 所谓类，就是对一类事物的描述，是抽象的、概念上的定义，比如鸟类，就泛指所有具有鸟类特征的动物。比如人类，不同的人，有着不同的性格、不同的爱好、不同的样貌等等，但是他们根本上都是人，所以说可以将他们抽象描述为人类。

对象是某一类事物实际存在的每个个体，因而也被称为实例（instance）我们每个人都是人类的一个实际存在的个体。

![image-20220919203119479](https://s2.loli.net/2022/09/19/U2P7qWOtRz5bhFY.png)

所以说，类就是抽象概念的人，而对象，就是具体的某一个人。

* A：是谁拿走了我的手机？
* B：是个人。（某一个类）
* A：我还知道是个人呢，具体是谁呢？
* B：是XXX。（具体某个对象）

而我们在Java中，也可以像这样进行编程，我们可以定义一个类，然后进一步创建许多这个类的实例对象。像这种编程方式，我们称为**面向对象编程**。

### 类的定义与对象创建

前面我们介绍了什么是类，什么是对象，首先我们就来看看如何去定义一个类。

比如现在我们想要定义一个人类，我们可以右键`src`目录，点击创建新的类：

![image-20220919204004526](https://s2.loli.net/2022/09/19/alOtdE1JNcbpxM8.png)

我们在对类进行命名时，一般使用英文单词，并且首字母大写，跟变量命名一样，不能出现任何的特殊字符。

![image-20220919204159248](https://s2.loli.net/2022/09/19/n1WuVYRiPeOfHqZ.png)

可以看到，现在我们的目录下有了两个`.java`源文件，其中一个是默认创建的Main.java，还有一个是我们刚刚创建的类。

我们来看看创建好之后，一个类写了哪些内容：

```java
public class Person {
    
}
```

可以发现，这不是跟一开始创建的Main中写的格式一模一样吗？没错，Main也是一个类，只不过我们一直都将其当做主类在使用，也就是编写主方法的类，关于方法我们会在后面进行介绍。

现在我们就创建好了一个类，既然是人类，那么肯定有人相关的一些属性，比如名字、性别、年龄等等，那么怎么才能给这个类添加一些属性呢？

我们可以将这些属性直接作为类的成员变量（成员变量相当于是这个类所具有的属性，每个实例创建出来之后，这些属性都可能会各不相同）定义到类中。

```java
public class Person {   //这里定义的人类具有三个属性，名字、年龄、性别
    String name;   //直接在类中定义变量，表示类具有的属性
    int age;
    String sex;
}
```

可能会有小伙伴疑问，这些变量啥时候被赋值呢？实际上这些变量只有在一个具体的对象中才可以使用。

那么现在人类的属性都规定好了，我们就可以尝试创建一个实例对象了，实例对应的应该是一个具体的人：

```java
new 类名();
```

```java
public static void main(String[] args) {
    new Person();   //我们可以使用new关键字来创建某个类的对象，注意new后面需要跟上 类名()
  	//这里创建出来的，就是一个具体的人了
}
```

实际上整个流程为：

![image-20220919205550104](https://s2.loli.net/2022/09/19/dSM4XDBV7qkIUlb.png)

只不过这里仅仅是创建出了这样的一个对象，我们目前没有办法去操作这个对象，比如想要修改或是获取这个人的名字等等。

### 对象的使用

既然现在我们知道如何创建对象，那么我们怎么去访问这个对象呢，比如我现在想要去查看或是修改它的名字。

我们同样可以使用一个变量来指代某个对象，只不过引用类型的变量，存储的是对象的引用，而不是对象本身：

```java
public static void main(String[] args) {
  	//这里的a存放的是具体的某个值
  	int a = 10;
  	//创建一个变量指代我们刚刚创建好的对象，变量的类型就是对应的类名
  	//这里的p存放的是对象的引用，而不是本体，我们可以通过对象的引用来间接操作对象
    Person p = new Person();
}
```

至于为什么对象类型的变量存放的是对象的引用，比如：

```java
public static void main(String[] args) {
    Person p1 = new Person();
    Person p2 = p1;
}
```

这里，我们将变量p2赋值为p1的值，那么实际上只是传递了对象的引用，而不是对象本身的复制，这跟我们前面的基本数据类型有些不同，p2和p1都指向的是同一个对象（如果你学习过C语言，它就类似于指针一样的存在）

![image-20220919211443657](https://s2.loli.net/2022/09/19/GBPaNZsr2MSKvCq.png)

我们可以来测试一下：

```java
public static void main(String[] args) {
    Person p1 = new Person();
    Person p2 = p1;
    System.out.println(p1 == p2);    //使用 == 可以判断两个变量引用的是不是同一个对象
}
```

但是如果我们像这样去编写：

```java
public static void main(String[] args) {
    Person p1 = new Person();   //这两个变量分别引用的是不同的两个对象
    Person p2 = new Person();
    System.out.println(p1 == p2);   //如果两个变量存放的是不同对象的引用，那么肯定就是不一样的了
}
```

实际上我们之前使用的String类型，也是一个引用类型，我们会在下一章详细讨论。我们在上一章介绍的都是基本类型，而类使用的都是引用类型。

现在我们有了对象的引用之后，我们就可以进行操作了：

![image-20220919210058797](https://s2.loli.net/2022/09/19/cEJ1CWshtQFbZzy.png)

我们可以直接访问对象的一些属性，也就是我们在类中定义好的那些，对于不同的对象，这些属性都具体存放值也会不同。

比如我们可以修改对象的名字：

```java
public static void main(String[] args) {
    Person p = new Person();
    p.name = "小明";   //要访问对象的属性，我们需要使用 . 运算符
    System.out.println(p.name);   //直接打印对象的名字，就是我们刚刚修改好的结果了
}
```

注意，不同对象的属性是分开独立存放的，每个对象都有一个自己的空间，修改一个对象的属性并不会影响到其他对象：

```java
public static void main(String[] args) {
    Person p1 = new Person();
    Person p2 = new Person();
    p1.name = "小明";   //这个修改的是第一个对象的属性
    p2.name = "大明";   //这里修改的是第二个对象的属性
    System.out.println(p1.name);  //这里我们获取的是第一个对象的属性
}
```

关于对象类型的变量，我们也可以不对任何对象进行引用：

```java
public static void main(String[] args) {
    Person p1 = null;  //null是一个特殊的值，它表示空，也就是不引用任何的对象
}
```

注意，如果不引用任何的对象，那肯定是不应该去通过这个变量去操作所引用的对象的（都没有引用对象，我操作谁啊我）

虽然这样可以编译通过，但是在运行时会出现问题：

```java
public static void main(String[] args) {
    Person p = null;   //此时变量没有引用任何对象
    p.name = "小红";   //我任性，就是要操作
    System.out.println(p.name);
}
```

我们来尝试运行一下这段代码：

![image-20220919213732810](https://s2.loli.net/2022/09/19/hkME1wf58aSdWGZ.png)

此时程序在运行的过程中，出现了异常，虽然我们还没有学习到异常，但是各位可以将异常理解为程序在运行过程中出现了问题，此时不得不终止程序退出。

这里出现的是空指针异常，很明显是因为我们去操作一个值为null的变量导致的。在我们以后的学习中，这个异常是出现频率最高的。

我们来看最后一个问题，对象创建成功之后，它的属性没有进行赋值，但是我们前面说了，变量使用之前需要先赋值，那么创建对象之后能否直接访问呢？

```java
public static void main(String[] args) {
    Person p = new Person();
    System.out.println("name = "+p.name);
    System.out.println("age = "+p.age);
    System.out.println("sex = "+p.sex);
}
```

我们来看看运行结果：

![image-20220919214248053](https://s2.loli.net/2022/09/19/zDRdFwhm6nebSJU.png)

我们可以看到，如果直接创建对象，那么对象的属性都会存在初始值，如果是基本类型，那么默认是统一为`0`（如果是boolean的话，默认值为false）如果是引用类型，那么默认是`null`。

### 方法创建与使用

前面我们介绍了类的定义以及对象的创建和使用。

现在我们的类有了属性，我们可以为创建的这些对象设定不同的属性值，比如每个人的名字都不一样，性别不一样，年龄不一样等等。只不过光有属性还不行，对象还需要具有一定的行为，就像我们人可以行走，可以跳跃，可以思考一样。

而对象也可以做出一些行为，我们可以通过定义方法来实现（在C语言中叫做函数）

方法是语句的集合，是为了完成某件事情而存在的。完成某件事情，可以有结果，也可以做了就做了，不返回结果。比如计算两个数字的和，我们需要得到计算后的结果，所以说方法需要有返回值；又比如，我们只想吧数字打印在控制台，只需要打印就行，不用给我结果，所以说方法不需要有返回值。

方法的定义如下：

```
返回值类型 方法名称() {
		方法体...
}
```

首先是返回值类型，也就是说这个方法完成任务之后，得到的结果的数据类型（可以是基本类型，也可以是引用类型）当然，如果没有返回值，只是完成任务，那么可以使用`void`表示没有返回值，比如我们现在给人类编写一个自我介绍的行为：

```java
public class Person {
    String name;
    int age;
    String sex;

  	//自我介绍只需要完成就行，没有返回值，所以说使用void
    void hello(){
      	//完成自我介绍需要执行的所有代码就在这个花括号中编写
      	//这里编写代码跟我们之前在main中是一样的（实际上main就是一个函数）
      	//自我介绍需要用到当前对象的名字和年龄，我们直接使用成员变量即可，变量的值就是当前对象的存放值
        System.out.println("我叫 "+name+" 今年 "+age+" 岁了！");
    }
}
```

注意，方法名称同样可以随便起，但是规则跟变量的命名差不多，也是尽量使用小写字母开头的单词，如果是多个单词，一般使用驼峰命名法最规范。

![image-20220920101033325](https://s2.loli.net/2022/09/20/2vmhsCRXpPzojiD.png)

现在我们给人类定义好了一个方法（行为）那么怎么才能让对象执行这个行为呢？

```java
public static void main(String[] args) {
    Person p = new Person();
    p.name = "小明";
    p.age = 18;
    p.hello();    //我们只需要使用 . 运算符，就可以执行定义好的方法了，只需要 .方法名称() 即可
}
```

像这样执行定义好的方法，我们一般称为**方法的调用**，我们来看看效果：

![image-20220919220837991](https://s2.loli.net/2022/09/19/bR2PAWoJ8qUzCfh.png)

比如现在我们要让人类学会加法运算，我们也可以通过定义一个方法的形式来完成，只不过，要完成加法运算，我们需要别人给人类提供两个参与加法运算的值才可以，所以我们这里就要用到参数了：

```java
//我们的方法需要别人提供参与运算的值才可以
//我们可以为方法设定参数，在调用方法时，需要外部传入参数才可以
//参数的定义需要在小括号内部编写，类似于变量定义，需要填写 类型和参数名称，多个参数用逗号隔开
int sum(int a, int b){   //这里需要两个int类型的参数进行计算

}
```

那么现在参数从外部传入之后，我们怎么使用呢？

```java
int sum(int a, int b){   //这里的参数，相当于我们在函数中定义了两个局部变量，我们可以直接在方法中使用
    int c = a + b;   //直接c = a + b
}
```

那么现在计算完成了，我们该怎么将结果传递到外面呢？首先函数的返回值是int类型，我们只需要使用`return`关键字来返回一个int类型的结果就可以了：

```java
int sum(int a, int b){
    int c = a + b;
    return c;   //return后面紧跟需要返回的结果，这样就可以将计算结果丢出去了
  	//带返回值的方法，是一定要有一个返回结果的！否则无法通过编译！
}
```

我们来测试一下吧：

```java
public static void main(String[] args) {
    Person p = new Person();
    p.name = "小明";
    p.age = 18;
    int result = p.sum(10, 20);    //现在我们要让这个对象帮我们计算10 + 20的结果
    System.out.println(result);    //成功得到30，实际上这里的println也是在调用方法进行打印操作
}
```

**注意：** 方法定义时编写的参数，我们一般称为形式参数，而调用方法实际传入的参数，我们成为实际参数。

是不是越来越感觉我们真的在跟一个对象进行交互？只要各位有了这样的体验，基本上就已经摸到面向对象的门路了。

关于`return`关键字，我们还需要进行进一步的介绍。

在我们使用`return`关键字之后，方法就会直接结束并返回结果，所以说在这之后编写的任何代码，都是不可到达的：

![image-20220919222813469](https://s2.loli.net/2022/09/19/UCcAb9L8lfOzXMZ.png)

在`return`后编写代码，会导致编译不通过，因为存在不可达语句。

如果我们的程序中出现了分支语句，那么必须保证每一个分支都有返回值才可以：

![image-20220919223037197](https://s2.loli.net/2022/09/19/WjUlRrPwA9EXThV.png)

只要有任何一个分支缺少了`return`语句，都无法正常通过编译，总之就是必须考虑到所有的情况，任何情况下都必须要有返回值。

当然，如果方法没有返回值，我们也可以使用`return`语句，不需要跟上任何内容，只不过这种情况下使用，仅仅是为了快速结束方法的执行：

```java
void test(int a){
    if(a == 10) return;    //当a等于10时直接结束方法，后面无论有没有代码都不会执行了
    System.out.println("Hello World!");   //不是的情况就正常执行
}
```

最后我们来讨论一下参数的传递问题：

```java
void test(int a){   //我们可以设置参数来让外部的数据传入到函数内部
    System.out.println(a);
}
```

实际上参数的传递，会在调用方法的时候，对参数的值进行复制，方法中的参数变量，不是我们传入的变量本身，我们来下面的这个例子：

```java
void swap(int a, int b){   //这个函数的目的很明显，就是为了交换a和b的值
    int tmp = a;
    a = b;
    b = a;
}
```

那么我们来测试一下：

```java
public static void main(String[] args) {
    Person p = new Person();
    int a = 5, b = 9;   //外面也叫a和b
    p.swap(a, b);
    System.out.println("a = "+a+", b = "+b);   //最后的结果会变成什么样子呢？
}
```

我们来看看结果是什么：

![image-20220919224219071](https://s2.loli.net/2022/09/19/wJrLaT7YBeQipNV.png)

我们发现a和b的值并没有发生交换，但是按照我们的方法逻辑来说，应该是会交换才对，这是为什么呢？实际上这里仅仅是将值复制给了函数里面的变量而已（相当于是变量的赋值）

![image-20220919224623727](https://s2.loli.net/2022/09/19/WdiDToucsCvySNf.png)

所以说我们交换的仅仅是方法中的a和b，参数传递仅仅是值传递，我们是没有办法直接操作到外面的a和b的。

那么各位小伙伴看看下面的例子：

```java
void modify(Person person){
    person.name = "lbwnb";   //修改对象的名称
}
```

```java
public static void main(String[] args) {
    Person p = new Person();
    p.name = "小明";     //先在外面修改一次
    p.modify(p);        //调用方法再修改一次
    System.out.println(p.name);    //请问最后name会是什么？
}
```

我们来看看结果：

![image-20220919224957971](https://s2.loli.net/2022/09/19/sNLjlYP6g3yxpe1.png)

不对啊，前面不是说只是值传递吗，怎么这里又可以修改成功呢？

确实，这里同样是进行的值传递，只不过各位小伙伴别忘了，我们前面可是说的清清楚楚，引用类型的变量，仅仅存放的是对象的引用，而不是对象本身。那么这里进行了值传递，相当于将对象的引用复制到了方法内部的变量中，而这个内部的变量，依然是引用的同一个对象，所以说这里在方法内操作，相当于直接操作外面的定义对象。

![image-20220919225455752](https://s2.loli.net/2022/09/19/aXf6AsdLneKxi9V.png)

### 方法进阶使用

有时候我们的方法中可能会出现一些与成员变量重名的变量：

```java
//我们希望使用这个方法，来为当前对象设定名字
void setName(String name) {
   
}
```

此时类中定义的变量名称也是`name`，那么我们是否可以这样编写呢：

```java
void setName(String name) {
    name = name;    //出现重名时，优先使用作用域最接近的，这里实际上是将方法参数的局部变量name赋值为本身
}
```

我们来测试一下：

```java
public static void main(String[] args) {
    Person p = new Person();
    p.setName("小明");
    System.out.println(p.name);
}
```

我们发现，似乎这样做并没有任何的效果，name依然是没有修改的状态。那么当出现重名的时候，因为默认情况下会优先使用作用域最近的变量，我们怎么才能表示要使用的变量是类的成员变量呢？

```java
Person p = new Person();
p.name = "小明";    //我们之前在外面使用时，可以直接通过对象.属性的形式访问到
```

同样的，我们如果想要在方法中访问到当前对象的属性，那么可以使用`this`关键字，来明确表示当前类的示例对象本身：

```java
void setName(String name) {
    this.name = name;   //让当前对象的name变量值等于参数传入的值
}
```

这样就可以修改成功了，当然，如果方法内没有变量出现重名的情况，那么默认情况下可以不使用`this`关键字来明确表示当前对象：

```java
String getName() {
    return name;    //这里没有使用this，但是当前作用域下只有对象属性的name变量，所以说直接就使用了
}
```

我们接着来看方法的重载。

有些时候，参数类型可能会多种多样，我们的方法需要能够同时应对多种情况：

```java
int sum(int a, int b){
    return a + b;
}
```

```java
public static void main(String[] args) {
    Person p = new Person();
    System.out.println(p.sum(10, 20));    //这里可以正常计算两个整数的和
}
```

但是要是我们现在不仅要让人类会计算整数，还要会计算小数呢？

![image-20220920102347110](https://s2.loli.net/2022/09/20/m7BvM1RctLznhrA.png)

当我们使用小数时，可以看到，参数要求的是int类型，那么肯定会出现错误，这个方法只能用于计算整数。此时，为了让这个方法支持使用小数进行计算，我们可以将这个方法进行重载。

一个类中可以包含多个同名的方法，但是需要的形式参数不一样，方法的返回类型，可以相同，也可以不同，但是仅返回类型不同，是不允许的！

```java
int sum(int a, int b){
    return a + b;
}

double sum(double a, double b){    //为了支持小数加法，我们可以进行一次重载
    return a + b;
}
```

这样就可以正常使用了：

```java
public static void main(String[] args) {
    Person p = new Person();
  	//当方法出现多个重载的情况，在调用时会自动进行匹配，选择合适的方法进行调用
    System.out.println(p.sum(1.5, 2.2));
}
```

包括我们之前一直在使用的`println`方法，其实也是重载了很多次的，因为要支持各种值的打印。

注意，如果仅仅是返回值的不同，是不支持重载的：

![image-20220920102933047](https://s2.loli.net/2022/09/20/N2TRuqEnxrKbpc8.png)

当然，方法之间是可以相互调用的：

```java
void test(){
    System.out.println("我是test");   //实际上这里也是调用另一个方法
}

void say(){
    test();   //在一个方法内调用另一个方法
}
```

如果我们这样写的话：

```java
void test(){
    say();
}

void say(){
    test();
}
```

各位猜猜看会出现什么情况？

![image-20220921001914601](https://s2.loli.net/2022/09/21/XPMVa3pdBcFICTE.png)

此时又出现了一个我们不认识的异常，实际上什么原因导致的我们自己都很清楚，方法之间一直在相互调用，没有一个出口。

方法自己也可以调用自己：

```java
void test(){
    test();
}
```

像这样自己调用自己的行为，我们称为递归调用，如果直接这样编写，会跟上面一样，出现栈溢出错误。但是如果我们给其合理地设置出口，就不会出现这种问题，比如我们想要计算从1加到n的和：

```java
int test(int n){
    if(n == 0) return 0;
    return test(n - 1) + n;    //返回的结果是下一层返回的结果+当前这一层的n
}
```

是不是感觉很巧妙？实际上递归调用在很多情况下能够快速解决一些很麻烦的问题，我们会在后面继续了解。

### 构造方法

我们接着来看一种比较特殊的方法，构造方法。

我们前面创建对象，都是直接使用`new`关键字就能直接搞定了，但是我们发现，对象在创建之后，各种属性都是默认值，那么能否实现在对象创建时就为其指定名字、年龄、性别呢？要在对象创建时进行处理，我们可以使用构造方法（构造器）来完成。

实际上每个类都有一个默认的构造方法，我们可以来看看反编译的结果：

```java
public class Person {
    String name;
    int age;
    String sex;

    public Person() {    //反编译中，多出来了这样一个方法，这其实就是构造方法
    }
}
```

构造方法不需要填写返回值，并且方法名称与类名相同，默认情况下每个类都会自带一个没有任何参数的无参构造方法（只是不用我们去写，编译出来就自带）当然，我们也可以手动声明，对其进行修改：

```java
public class Person {
    String name;
    int age;
    String sex;

    Person(){    //构造方法不需要指定返回值，并且方法名称与类名相同
        name = "小明";   //构造方法会在对象创建时执行，我们可以将各种需要初始化的操作都在这里进行处理
        age = 18;
        sex = "男";
    }
}
```

构造方法会在new的时候自动执行：

```java
public static void main(String[] args) {
    Person p = new Person();   //这里的new Person()其实就是在调用无参构造方法
    System.out.println(p.name);
}
```

当然，我们也可以为构造方法设定参数：

```java
public class Person {
    String name;
    int age;
    String sex;

    Person(String name, int age, String sex){   //跟普通方法是一样的
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
```

注意，在我们自己定义一个构造方法之后，会覆盖掉默认的那一个无参构造方法，除非我们手动重载一个无参构造，否则要创建这个类的对象，必须调用我们自己定义的构造方法：

```java
public static void main(String[] args) {
    Person p = new Person("小明", 18, "男");   //调用自己定义的带三个参数的构造方法
    System.out.println(p.name);
}
```

我们可以去看看反编译的结果，会发现此时没有无参构造了，而是只剩下我们自己编写的。

当然，要给成员变量设定初始值，我们不仅可以通过构造方法，也可以直接在定义时赋值：

```java
public class Person {
    String name = "未知";   //直接赋值，那么对象构造好之后，属性默认就是这个值
    int age = 10;
    String sex = "男";
}
```

这里需要特别注意，成员变量的初始化，并不是在构造方法之前之后，而是在这之前就已经完成了：

```java
Person(String name, int age, String sex){
    System.out.println(age);    //在赋值之前看看是否有初始值
    this.name = name;
    this.age = age;
    this.sex = sex;
}
```

我们也可以在类中添加代码块，代码块同样会在对象构造之前进行，在成员变量初始化之后执行：

```java
public class Person {
    String name;
    int age;
    String sex;

    {
        System.out.println("我是代码块");   //代码块中的内容会在对象创建时仅执行一次
    }

    Person(String name, int age, String sex){
        System.out.println("我被构造了");
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
```

只不过一般情况下使用代码块的频率比较低，标准情况下还是通过构造方法进行进行对象初始化工作，所以说这里做了解就行了。

### 静态变量和静态方法

前面我们已经了解了类的大部分特性，一个类可以具有多种属性、行为，包括对象该如何创建，我们可以通过构造方法进行设定，我们可以通过类创建对象，每个对象都会具有我们在类中设定好的属性，包括我们设定好的行为，所以说类就像是一个模板，我们可以通过这个模板快速捏造出一个又一个的对象。我们接着来看比较特殊的静态特性。

静态的内容，我们可以理解为是属于这个类的，也可以理解为是所有对象共享的内容。我们通过使用`static`关键字来声明一个变量或一个方法为静态的，一旦被声明为静态，那么通过这个类创建的所有对象，操作的都是同一个目标，也就是说，对象再多，也只有这一个静态的变量或方法。一个对象改变了静态变量的值，那么其他的对象读取的就是被改变的值。

```java
public class Person {
    String name;
    int age;
    String sex;
    static String info;    //这里我们定义一个info静态变量
}
```

我们来测试一下：

```java
public static void main(String[] args) {
    Person p1 = new Person();
    Person p2 = new Person();
    p1.info = "杰哥你干嘛";
    System.out.println(p2.info);   //可以看到，由于静态属性是属于类的，因此无论通过什么方式改变，都改变的是同一个目标
}
```

所以说一般情况下，我们并不会通过一个具体的对象去修改和使用静态属性，而是通过这个类去使用：

```java
public static void main(String[] args) {
    Person.info = "让我看看";
    System.out.println(Person.info);
}
```

同样的，我们可以将方法标记为静态：

```java
static void test(){
    System.out.println("我是静态方法");
}
```

静态方法同样是属于类的，而不是具体的某个对象，所以说，就像下面这样：

![image-20220920234401275](https://s2.loli.net/2022/09/20/cWCrJgnkXFL63y2.png)

因为静态方法属于类的，所以说我们在静态方法中，无法获取成员变量的值：

![image-20220920235418115](https://s2.loli.net/2022/09/20/XvPjtLm2wOMh4ZK.png)

成员变量是某个具体对象拥有的属性，就像小明这个具体的人的名字才叫小明，而静态方法是类具有的，并不是具体对象的，肯定是没办法访问到的。同样的，在静态方法中，无法使用`this`关键字，因为this关键字代表的是当前的对象本身。

但是静态方法是可以访问到静态变量的：

```java
static String info;

static void test(){
    System.out.println("静态变量的值为："+info);
}
```

因为他们都属于类，所以说肯定是可以访问到的。

我们也可以将代码块变成静态的：

```java
static String info;

static {   //静态代码块可以用于初始化静态变量
    info = "测试";
}
```

那么，静态变量，是在什么时候进行初始化的呢？

我们在一开始介绍了，我们实际上是将`.class`文件丢给JVM去执行的，而每一个`.class`文件其实就是我们编写的一个类，我们在Java中使用一个类之前，JVM并不会在一开始就去加载它，而是在需要时才会去加载（优化）一般遇到以下情况时才会会加载类：

- 访问类的静态变量，或者为静态变量赋值
- new 创建类的实例（隐式加载）
- 调用类的静态方法
- 子类初始化时
- 其他的情况会在讲到反射时介绍

所有被标记为静态的内容，会在类刚加载的时候就分配，而不是在对象创建的时候分配，所以说静态内容一定会在第一个对象初始化之前完成加载。

我们可以来测试一下：

```java
public class Person {
    String name = test();  //这里我们用test方法的返回值作为变量的初始值，便于观察
    int age;
    String sex;

    {
        System.out.println("我是普通代码块");
    }
    
    Person(){
        System.out.println("我是构造方法");
    }
    
    String test(){
        System.out.println("我是成员变量初始化");
        return "小明";
    }

    static String info = init();   //这里我们用init静态方法的返回值作为变量的初始值，便于观察

    static {
        System.out.println("我是静态代码块");
    }

    static String init(){
        System.out.println("我是静态变量初始化");
        return "test";
    }
}
```

现在我们在主方法中创建一个对象，观察这几步是怎么在执行的：

![image-20220921000953525](https://s2.loli.net/2022/09/21/JxTPk8SWtDmK6IX.png)

可以看到，确实是静态内容在对象构造之前的就完成了初始化，实际上就是类初始化时完成的。

当然，如果我们直接访问类的静态变量：

```java
public static void main(String[] args) {
    System.out.println(Person.info);
}
```

那么此时同样会使得类初始化，进行加载：

![image-20220921001222465](https://s2.loli.net/2022/09/21/auMJOvNfx9K3mzd.png)

可以看到，在使用时，确实是先将静态内容初始化之后，才得到值的。当然，如果我们压根就没有去使用这个类，那么也不会被初始化了。

有关类与对象的基本内容，我们就全部讲解完毕了。

***

## 包和访问控制

通过前面的学习，我们知道该如何创建和使用类。

### 包声明和导入

包其实就是用来区分类位置的东西，也可以用来将我们的类进行分类（类似于C++中的namespace）随着我们的程序不断变大，可能会创建各种各样的类，他们可能会做不同的事情，那么这些类如果都放在一起的话，有点混乱，我们可以通过包的形式将这些类进行分类存放。

包的命名规则同样是英文和数字的组合，最好是一个域名的格式，比如我们经常访问的`www.baidu.com`，后面的baidu.com就是域名，我们的包就可以命名为`com.baidu`，当然，各位小伙伴现在还没有自己的域名，所以说我们随便起一个名称就可以了。其中的`.`就是用于分割的，对应多个文件夹，比如`com.test`：

![image-20220921120040350](https://s2.loli.net/2022/09/21/OZdDi1sGluyjbgr.png)

我们可以将类放入到包中：

![image-20220921115055000](https://s2.loli.net/2022/09/21/e3GvFsHDhMAtBWR.png)

我们之前都是直接创建的类，所以说没有包这个概念，但是现在，我们将类放到包中，就需要注意了：

```java
package com.test;   //在放入包中，需要在类的最上面添加package关键字来指明当前类所处的包

public class Main {   //将Main类放到com.test这个包中
    public static void main(String[] args) {

    }
}
```

这里又是一个新的关键字`package`，这个是用于指定当前类所处的包的，注意，所处的包和对应的目录是一一对应的。

不同的类可以放在不同的包下：

![image-20220921120241184](https://s2.loli.net/2022/09/21/stOGnxaPirZvjLF.png)

当我们使用同一个包中的类时，直接使用即可（之前就是直接使用的，因为都直接在一个缺省的包中）而当我们需要使用其他包中的类时，需要先进行导入才可以：

```java
package com.test;

import com.test.entity.Person;   //使用import关键字导入其他包中的类

public class Main {
    public static void main(String[] args) {
        Person person = new Person();   //只有导入之后才可以使用，否则编译器不知道这个类从哪来的
    }
}
```

这里使用了`import`关键字导入我们需要使用的类，当然，只有在类不在同一个包下时才需要进行导入，如果一个包中有多个类，我们可以使用`*`表示导入这个包中全部的类：

```java
import com.test.entity.*;
```

实际上我们之前一直在使用的`System`类，也是在一个包中的：

```java
package java.lang;

import java.io.*;
import java.lang.reflect.Executable;
import java.lang.annotation.Annotation;
import java.security.AccessControlContext;
import java.util.Properties;
import java.util.PropertyPermission;
import java.util.StringTokenizer;
import java.util.Map;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.AllPermission;
import java.nio.channels.Channel;
import java.nio.channels.spi.SelectorProvider;
import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.security.util.SecurityConstants;
import sun.reflect.annotation.AnnotationType;

import jdk.internal.util.StaticProperty;

/**
 * The <code>System</code> class contains several useful class fields
 * and methods. It cannot be instantiated.
 *
 * <p>Among the facilities provided by the <code>System</code> class
 * are standard input, standard output, and error output streams;
 * access to externally defined properties and environment
 * variables; a means of loading files and libraries; and a utility
 * method for quickly copying a portion of an array.
 *
 * @author  unascribed
 * @since   JDK1.0
 */
public final class System {
	  ...
}
```

可以看到它是属于`java.lang`这个包下的类，并且这个类也导入了很多其他包中的类在进行使用。那么，为什么我们在使用这个类时，没有导入呢？实际上Java中会默认导入`java.lang`这个包下的所有类，因此我们不需要手动指定。

IDEA非常智能，我们在使用项目中定义的类时，会自动帮我们将导入补全，所以说代码写起来非常高效。

注意，在不同包下的类，即使类名相同，也是不同的两个类：

```java
package com.test.entity;

public class String {    //我们在自己的包中也建一个名为String的类
}
```

当我们在使用时：

![image-20220921121404900](/Users/nagocoler/Library/Application Support/typora-user-images/image-20220921121404900.png)

由于默认导入了系统自带的String类，并且也导入了我们自己定义的String类，那么此时就出现了歧义，编译器不知道到底我们想用的是哪一个String类，所以说我们需要明确指定：

```java
public class Main {
    public static void main(java.lang.String[] args) {   //主方法的String参数是java.lang包下的，我们需要明确指定一下，只需要在类名前面添加包名就行了
				com.test.entity.String string = new com.test.entity.String();
    }
}
```

我们只需要在类名前面把完整的包名也给写上，就可以表示这个是哪一个包里的类了，当然，如果没有出现歧义，默认情况下包名是可以省略的，可写可不写。

可能各位小伙伴会发现一个问题，为什么对象的属性访问不了了？

![image-20220921122514457](https://s2.loli.net/2022/09/21/UaqMihmIQkzHFtG.png)

编译器说name属性在这个类中不是public，无法在外部进行访问，这是什么情况呢？这里我们就要介绍的到Java的访问权限控制了。

### 访问权限控制

实际上Java中是有访问权限控制的，就是我们个人的隐私的一样，我不允许别人随便来查看我们的隐私，只有我们自己同意的情况下，才能告诉别人我们的名字、年龄等隐私信息。

所以说Java中引入了访问权限控制（可见性），我们可以为成员变量、成员方法、静态变量、静态方法甚至是类指定访问权限，不同的访问权限，有着不同程度的访问限制：

* `private`   -   私有，标记为私有的内容无法被除当前类以外的任何位置访问。
* `什么都不写`   -   默认，默认情况下，只能被类本身和同包中的其他类访问。
* `protected`   -   受保护，标记为受保护的内容可以能被类本身和同包中的其他类访问，也可以被子类访问（子类我们会在下一章介绍）
* `public`    -   公共，标记为公共的内容，允许在任何地方被访问。

这四种访问权限，总结如下表：

|           | 当前类 | 同一个包下的类 | 不同包下的子类 | 不同包下的类 |
| :-------: | :----: | :------------: | :------------: | :----------: |
|  public   |   ✅    |       ✅        |       ✅        |      ✅       |
| protected |   ✅    |       ✅        |       ✅        |      ❌       |
|   默认    |   ✅    |       ✅        |       ❌        |      ❌       |
|  private  |   ✅    |       ❌        |       ❌        |      ❌       |

比如我们刚刚出现的情况，就是因为是默认的访问权限，所以说在当前包以外的其他包中无法访问，但是我们可以提升它的访问权限，来使得外部也可以访问：

```java
public class Person {
    public String name;   //在name变量前添加public关键字，将其可见性提升为公共等级
    int age;
    String sex;
}
```

这样我们就可以在外部正常使用这个属性了：

```java
public static void main(String[] args) {
    Person person = new Person();
    System.out.println(person.name);   //正常访问到成员变量
}
```

实际上如果各位小伙伴观察仔细的话，会发现我们创建出来的类自带的访问等级就是`public`：

```java
package com.test.entity;

public class Person {   //class前面有public关键字

}
```

也就是说这个类实际上可以在任何地方使用，但是我们也可以将其修改为默认的访问等级：

```java
package com.test.entity;

class Person {    //去掉public变成默认等级
  
}
```

如果是默认等级的话，那么在外部同样是无法访问的：

![image-20220921142724239](https://s2.loli.net/2022/09/21/ZTRAEItQY6UcqvP.png)

但是注意，我们创建的普通类不能是`protected`或是`private`权限，因为我们目前所使用的普通类要么就是只给当前的包内使用，要么就是给外面都用，如果是`private`谁都不能用，那这个类定义出来干嘛呢？

如果某个类中存在静态方法或是静态变量，那么我们可以通过静态导入的方式将其中的静态方法或是静态变量直接导入使用，但是同样需要有访问权限的情况下才可以：

```java
public class Person {
    String name;
    int age;
    String sex;
    
    public static void test(){
        System.out.println("我是静态方法！");
    }
}
```

我们来尝试一下静态导入：

```java
import static com.test.entity.Person.test;    //静态导入test方法

public class Main {
    public static void main(String[] args) {
        test();    //直接使用就可以，就像在这个类定义的方法一样
    }
}
```

至此，有关包相关的内容，我们就讲解到这里。

***

## 封装、继承和多态

封装、继承和多态是面向对象编程的三大特性。

> 封装，把对象的属性和方法结合成一个独立的整体，隐藏实现细节，并提供对外访问的接口。
>
> 继承，从已知的一个类中派生出一个新的类，叫子类。子类实现了父类所有非私有化的属性和方法，并根据实际需求扩展出新的行为。
>
> 多态，多个不同的对象对同一消息作出响应，同一消息根据不同的对象而采用各种不同的方法。

正是这三大特性，让我们的Java程序更加生动形象。

### 类的封装

封装的目的是为了保证变量的安全性，使用者不必在意具体实现细节，而只是通过外部接口即可访问类的成员，如果不进行封装，类中的实例变量可以直接查看和修改，可能给整个代码带来不好的影响，因此在编写类时一般将成员变量私有化，外部类需要使用Getter和Setter方法来查看和设置变量。

我们可以将之前的类进行改进：

```java
public class Person {
    private String name;    //现在类的属性只能被自己直接访问
    private int age;
    private String sex;
  
  	public Person(String name, int age, String sex) {   //构造方法也要声明为公共，否则对象都构造不了
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;    //想要知道这个对象的名字，必须通过getName()方法来获取，并且得到的只是名字值，外部无法修改
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }
}
```

我们可以来试一下：

```java
public static void main(String[] args) {
    Person person = new Person("小明", 18, "男");
    System.out.println(person.getName());    //只能通过调用getName()方法来获取名字
}
```

也就是说，外部现在只能通过调用我定义的方法来获取成员属性，而我们可以在这个方法中进行一些额外的操作，比如小明可以修改名字，但是名字中不能包含"小"这个字：

```java
public void setName(String name) {
    if(name.contains("小")) return;
    this.name = name;
}
```

我们甚至还可以将构造方法改成私有的，需要通过我们的内部的方式来构造对象：

```java
public class Person {
    private String name;
    private int age;
    private String sex;

    private Person(){}   //不允许外部使用new关键字创建对象
    
    public static Person getInstance() {   //而是需要使用我们的独特方法来生成对象并返回
        return new Person();
    }
}
```

通过这种方式，我们可以实现单例模式：

> ```java
> public class Test {
>     private static Test instance;
>     
>     private Test(){}
>     
>     public static Test getInstance() {
>         if(instance == null) 
>             instance = new Test();
>         return instance;
>     }
> }
> ```
>
> 单例模式就是全局只能使用这一个对象，不能创建更多的对象，我们就可以封装成这样，关于单例模式的详细介绍，还请各位小伙伴在《Java设计模式》视频教程中再进行学习。

封装思想其实就是把实现细节给隐藏了，外部只需知道这个方法是什么作用，而无需关心实现，要用什么由类自己来做，不需要外面来操作类内部的东西去完成，封装就是通过访问权限控制来实现的。

### 类的继承

前面我们介绍了类的封装，我们接着来看一个非常重要特性：继承。

在定义不同类的时候存在一些相同属性，为了方便使用可以将这些共同属性抽象成一个父类，在定义其他子类时可以继承自该父类，减少代码的重复定义，子类可以使用父类中**非私有**的成员。

比如说我们一开始使用的人类，那么实际上人类根据职业划分，所掌握的技能也会不同，比如画家会画画，歌手会唱，舞者会跳，Rapper会rap，运动员会篮球，我们可以将人类这个大类根据职业进一步地细分出来：

![image-20220921150139125](https://s2.loli.net/2022/09/21/zlZ9JXAjvxpawPF.png)

实际上这些划分出来的类，本质上还是人类，也就是说人类具有的属性，这些划分出来的类同样具有，但是，这些划分出来的类同时也会拥有他们自己独特的技能。在Java中，我们可以创建一个类的子类来实现上面的这种效果：

```java
public class Person {   //先定义一个父类
    String name;
    int age;
    String sex;
}
```

接着我们可以创建各种各样的子类，想要继承一个类，我们只需要使用`extends`关键字即可：

```java
public class Worker extends Person{    //工人类
    
}
```

```java
public class Student extends Person{   //学生类

}
```

类的继承可以不断向下，但是同时只能继承一个类，同时，标记为`final`的类不允许被继承：

```java
public final class Person {  //class前面添加final关键字表示这个类已经是最终形态，不能继承
  
}
```

当一个类继承另一个类时，属性会被继承，可以直接访问父类中定义的属性，除非父类中将属性的访问权限修改为`private`，那么子类将无法访问（但是依然是继承了这个属性的）：

```java
public class Student extends Person{
    public void study(){
        System.out.println("我的名字是 "+name+"，我在学习！");   //可以直接访问父类中定义的name属性
    }
}
```

同样的，在父类中定义的方法同样会被子类继承：

```java
public class Person {
    String name;
    int age;
    String sex;

    public void hello(){
        System.out.println("我叫 "+name+"，今年 "+age+" 岁了!");
    }
}
```

子类直接获得了此方法，当我们创建一个子类对象时就可以直接使用这个方法：

```java
public static void main(String[] args) {
    Student student = new Student();
    student.study();    //子类不仅有自己的独特技能
    student.hello();    //还继承了父类的全部技能
}
```

是不是感觉非常人性化，子类继承了父类的全部能力，同时还可以扩展自己的独特能力，就像一句话说的： 龙生龙凤生凤，老鼠儿子会打洞。

如果父类存在一个有参构造方法，子类必须在构造方法中调用：

```java
public class Person {
    protected String name;   //因为子类需要用这些属性，所以说我们就将这些变成protected，外部不允许访问
    protected int age;
    protected String sex;
    protected String profession;

  	//构造方法也改成protected，只能子类用
    protected Person(String name, int age, String sex, String profession) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.profession = profession;
    }

    public void hello(){
        System.out.println("["+profession+"] 我叫 "+name+"，今年 "+age+" 岁了!");
    }
}
```

可以看到，此时两个子类都报错了：

![image-20220921153512798](https://s2.loli.net/2022/09/21/SgPjRtUN64bmWrX.png)

因为子类在构造时，不仅要初始化子类的属性，还需要初始化父类的属性，所以说在默认情况下，子类其实是调用了父类的构造方法的，只是在无参的情况下可以省略，但是现在父类构造方法需要参数，那么我们就需要手动指定了：

既然现在父类需要三个参数才能构造，那么子类需要按照同样的方式调用父类的构造方法：

```java
public class Student extends Person{
    public Student(String name, int age, String sex) {    //因为学生职业已经确定，所以说学生直接填写就可以了
        super(name, age, sex, "学生");   //使用super代表父类，父类的构造方法就是super()
    }

    public void study(){
        System.out.println("我的名字是 "+name+"，我在学习！");
    }
}
```

```java
public class Worker extends Person{
    public Worker(String name, int age, String sex) {
        super(name, age, sex, "工人");    //父类构造调用必须在最前面
        System.out.println("工人构造成功！");    //注意，在调用父类构造方法之前，不允许执行任何代码，只能在之后执行
    }
}
```

我们在使用子类时，可以将其当做父类来使用：

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");    //这里使用父类类型的变量，去引用一个子类对象（向上转型）
    person.hello();    //父类对象的引用相当于当做父类来使用，只能访问父类对象的内容
}
```

虽然我们这里使用的是父类类型引用的对象，但是这并不代表子类就彻底变成父类了，这里仅仅只是当做父类使用而已。

我们也可以使用强制类型转换，将一个被当做父类使用的子类对象，转换回子类：

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");
    Student student = (Student) person;   //使用强制类型转换（向下转型）
    student.study();
}
```

但是注意，这种方式只适用于这个对象本身就是对应的子类才可以，如果本身都不是这个子类，或者说就是父类，那么会出现问题：

```java
public static void main(String[] args) {
    Person person = new Worker("小明", 18, "男");   //实际创建的是Work类型的对象
    Student student = (Student) person;
    student.study();
}
```

![image-20220921160309835](https://s2.loli.net/2022/09/21/JdMLt19Yq6KQz4v.png)

此时直接出现了类型转换异常，因为本身不是这个类型，强转也没用。

那么如果我们想要判断一下某个变量所引用的对象到底是什么类，那么该怎么办呢？

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");
    if(person instanceof Student) {   //我们可以使用instanceof关键字来对类型进行判断
        System.out.println("对象是 Student 类型的");
    }
    if(person instanceof Person) {
        System.out.println("对象是 Person 类型的");
    }
}
```

如果变量所引用的对象是对应类型或是对应类型的子类，那么`instanceof`都会返回`true`，否则返回`false`。

最后我们需要来特别说明一下，子类是可以定义和父类同名的属性的：

```java
public class Worker extends Person{
    protected String name;   //子类中同样可以定义name属性
    
    public Worker(String name, int age, String sex) {
        super(name, age, sex, "工人");
    }
}
```

此时父类的name属性和子类的name属性是同时存在的，那么当我们在子类中直接使用时：

```java
public void work(){
    System.out.println("我是 "+name+"，我在工作！");   //这里的name，依然是作用域最近的哪一个，也就是在当前子类中定义的name属性，而不是父类的name属性
}
```

所以说，我们在使用时，实际上这里得到的结果为`null`：

![image-20220921160742714](https://s2.loli.net/2022/09/21/nKDaTJZ2LhEX3Hs.png)

那么，在子类存在同名变量的情况下，怎么去访问父类的呢？我们同样可以使用`super`关键字来表示父类：

```java
public void work(){
    System.out.println("我是 "+super.name+"，我在工作！");   //这里使用super.name来表示需要的是父类的name变量
}
```

这样得到的结果就不一样了：

![image-20220921160851193](https://s2.loli.net/2022/09/21/DobHL2CWRMIif3z.png)

但是注意，没有`super.super`这种用法，也就是说如果存在多级继承的话，那么最多只能通过这种方法访问到父类的属性（包括继承下来的属性）

### 顶层Object类

实际上所有类都默认继承自Object类，除非手动指定继承的类型，但是依然改变不了最顶层的父类是Object类。所有类都包含Object类中的方法，比如：

![image-20220921214642969](https://s2.loli.net/2022/09/21/FCHDEI4rTAQquas.png)

我们发现，除了我们自己在类中编写的方法之外，还可以调用一些其他的方法，那么这些方法不可能无缘无故地出现，肯定同样是因为继承得到的，那么这些方法是继承谁得到的呢？

```java
public class Person extends Object{   
//除非我们手动指定要继承的类是什么，实际上默认情况下所有的类都是继承自Object的，只是可以省略

}
```

所以说我们的继承结构差不多就是：

![image-20220921214944267](https://s2.loli.net/2022/09/21/hkapOYVHBrjy7UC.png)

既然所有的类都默认继承自Object，我们来看看这个类里面有哪些内容：

```java
public class Object {

    private static native void registerNatives();   //标记为native的方法是本地方法，底层是由C++实现的
    static {
        registerNatives();   //这个类在初始化时会对类中其他本地方法进行注册，本地方法不是我们SE中需要学习的内容，我们会在JVM篇视频教程中进行介绍
    }

    //获取当前的类型Class对象，这个我们会在最后一章的反射中进行讲解，目前暂时不会用到
    public final native Class<?> getClass();

    //获取对象的哈希值，我们会在第五章集合类中使用到，目前各位小伙伴就暂时理解为会返回对象存放的内存地址
    public native int hashCode();

  	//判断当前对象和给定对象是否相等，默认实现是直接用等号判断，也就是直接判断是否为同一个对象
  	public boolean equals(Object obj) {
        return (this == obj);
    }
  
    //克隆当前对象，可以将复制一个完全一样的对象出来，包括对象的各个属性
    protected native Object clone() throws CloneNotSupportedException;

    //将当前对象转换为String的形式，默认情况下格式为 完整类名@十六进制哈希值
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    //唤醒一个等待当前对象锁的线程，有关锁的内容，我们会在第六章多线程部分中讲解，目前暂时不会用到
    public final native void notify();

    //唤醒所有等待当前对象锁的线程，同上
    public final native void notifyAll();

    //使得持有当前对象锁的线程进入等待状态，同上
    public final native void wait(long timeout) throws InterruptedException;

    //同上
    public final void wait(long timeout, int nanos) throws InterruptedException {
        ...
    }

    //同上
    public final void wait() throws InterruptedException {
        ...
    }

    //当对象被判定为已经不再使用的“垃圾”时，在回收之前，会由JVM来调用一次此方法进行资源释放之类的操作，这同样不是SE中需要学习的内容，这个方法我们会在JVM篇视频教程中详细介绍，目前暂时不会用到
    protected void finalize() throws Throwable { }
}
```

这里我们可以尝试调用一下Object为我们提供的`toString()`方法：

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");
    String str = person.toString();
    System.out.println(str);
}
```

这里就是按照上面说的格式进行打印：

![image-20220921221053801](https://s2.loli.net/2022/09/21/hpBOjqf4iwJW1Pr.png)

当然，我们直接可以给`println`传入一个Object类型的对象：

```java
public void println(Object x) {
    String s = String.valueOf(x);   //这里同样会调用对象的toString方法，所以说跟上面效果是一样的
    synchronized (this) {
        print(s);
        newLine();
    }
}
```

有小伙伴肯定会好奇，这里不是接受的一个Object类型的值的，为什么任意类型都可以传入呢？因为所有类型都是继承自Object，如果方法接受的参数是一个引用类型的值，那只要是这个类的对象或是这个类的子类的对象，都可以作为参数传入。

我们也可以试试看默认提供的`equals`方法：

```java
public static void main(String[] args) {
    Person p1 = new Student("小明", 18, "男");
    Person p2 = new Student("小明", 18, "男");
    System.out.println(p1.equals(p2));
}
```

因为默认比较的是两个对象是否为同一个对象，所以说这里得到的肯定是false，但是有些情况下，实际上我们所希望的情况是如果名字、年龄、性别都完全相同，那么这肯定是同一个人，但是这里却做不到这样的判断，我们需要修改一下`equals`方法的默认实现来完成，这就要用到方法的重写了。

### 方法的重写

注意，方法的重写不同于之前的方法重载，不要搞混了，方法的重载是为某个方法提供更多种类，而方法的重写是覆盖原有的方法实现，比如我们现在不希望使用Object类中提供的`equals`方法，那么我们就可以将其重写了：

```java
public class Person{
    ...

    @Override   //重写方法可以添加 @Override 注解，有关注解我们会在最后一章进行介绍，这个注解默认情况下可以省略
    public boolean equals(Object obj) {   //重写方法要求与父类的定义完全一致
        if(obj == null) return false;   //如果传入的对象为null，那肯定不相等
        if(obj instanceof Person) {     //只有是当前类型的对象，才能进行比较，要是都不是这个类型还比什么
            Person person = (Person) obj;   //先转换为当前类型，接着我们对三个属性挨个进行比较
            return this.name.equals(person.name) &&    //字符串内容的比较，不能使用==，必须使用equals方法
                    this.age == person.age &&       //基本类型的比较跟之前一样，直接==
                    this.sex.equals(person.sex);
        }
        return false;
    }
}
```

在重写Object提供的`equals`方法之后，就会按照我们的方式进行判断了：

```java
public static void main(String[] args) {
    Person p1 = new Student("小明", 18, "男");
    Person p2 = new Student("小明", 18, "男");
    System.out.println(p1.equals(p2));   //此时由于三个属性完全一致，所以说判断结果为真，即使是两个不同的对象
}
```

有时候为了方便查看对象的各个属性，我们可以将Object类提供的`toString`方法重写了：

```java
@Override
public String toString() {    //使用IDEA可以快速生成
    return "Person{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", sex='" + sex + '\'' +
            ", profession='" + profession + '\'' +
            '}';
}
```

这样，我们直接打印对象时，就会打印出对象的各个属性值了：

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");
    System.out.println(person);
}
```

![image-20220921223249343](https://s2.loli.net/2022/09/21/FCAnxSUjhaLuXW8.png)

注意，静态方法不支持重写，因为它是属于类本身的，但是它可以被继承。

基于这种方法可以重写的特性，对于一个类定义的行为，不同的子类可以出现不同的行为，比如考试，学生考试可以得到A，而工人去考试只能得到D：

```java
public class Person {
    ...

    public void exam(){
        System.out.println("我是考试方法");
    }
  
  	...
}
```

```java
public class Student extends Person{
    ...

    @Override
    public void exam() {
        System.out.println("我是学生，我就是小镇做题家，拿个 A 轻轻松松");
    }
}
```

```java
public class Worker extends Person{
    ...

    @Override
    public void exam() {
        System.out.println("我是工人，做题我并不擅长，只能得到 D");
    }
}
```

这样，不同的子类，对于同一个方法会产生不同的结果：

```java
public static void main(String[] args) {
    Person person = new Student("小明", 18, "男");
    person.exam();

    person = new Worker("小强", 18, "男");
    person.exam();
}
```

![image-20220921224525855](https://s2.loli.net/2022/09/21/zogT67B91tJaHLD.png)

这其实就是面向对象编程中多态特性的一种体现。

注意，我们如果不希望子类重写某个方法，我们可以在方法前添加`final`关键字，表示这个方法已经是最终形态：

```java
public final void exam(){
    System.out.println("我是考试方法");
}
```

![image-20220921224907373](https://s2.loli.net/2022/09/21/zpKfDlGTLwx5iy8.png)

或者，如果父类中方法的可见性为`private`，那么子类同样无法访问，也就不能重写，但是可以定义同名方法：

![image-20220921225651487](https://s2.loli.net/2022/09/21/d9k21hyGL6WExZ3.png)

虽然这里可以编译通过，但是并不是对父类方法的重写，仅仅是子类自己创建的一个新方法。

还有，我们在重写父类方法时，如果希望调用父类原本的方法实现，那么同样可以使用`super`关键字：

```java
@Override
public void exam() {
    super.exam();   //调用父类的实现
    System.out.println("我是工人，做题我并不擅长，只能得到 D");
}
```

然后就是访问权限的问题，子类在重写父类方法时，不能降低父类方法中的可见性：

```java
public void exam(){
    System.out.println("我是考试方法");
}
```

![image-20220921225234226](https://s2.loli.net/2022/09/21/zfhZ3YdFeCgJu89.png)

因为子类实际上可以当做父类使用，如果子类的访问权限比父类还低，那么在被当做父类使用时，就可能出现无视访问权限调用的情况，这样肯定是不行的，但是相反的，我们可以在子类中提升权限：

```java
protected void exam(){
    System.out.println("我是考试方法");
}
```

```java
@Override
public void exam() {   //将可见性提升为public 
    System.out.println("我是工人，做题我并不擅长，只能得到 D");
}
```

![image-20220921225840122](https://s2.loli.net/2022/09/21/igvGNTQs2xKOZrI.png)

可以看到作为子类时就可以正常调用，但是如果将其作为父类使用，因为访问权限不足所有就无法使用，总之，子类重写的方法权限不能比父类还低。

### 抽象类

在我们学习了类的继承之后，实际上我们会发现，越是处于顶层定义的类，实际上可以进一步地进行抽象，比如我们前面编写的考试方法：

```java
protected void exam(){
    System.out.println("我是考试方法");
}
```

这个方法再子类中一定会被重写，所以说除非子类中调用父类的实现，否则一般情况下永远都不会被调用，就像我们说一个人会不会考试一样，实际上人怎么考试是一个抽象的概念，而学生怎么考试和工人怎么考试，才是具体的一个实现，所以说，我们可以将人类进行进一步的抽象，让某些方法完全由子类来实现，父类中不需要提供实现。

要实现这样的操作，我们可以将人类变成抽象类，抽象类比类还要抽象：

```java
public abstract class Person {   //通过添加abstract关键字，表示这个类是一个抽象类
    protected String name;   //大体内容其实普通类差不多
    protected int age;
    protected String sex;
    protected String profession;

    protected Person(String name, int age, String sex, String profession) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.profession = profession;
    }

    public abstract void exam();   //抽象类中可以具有抽象方法，也就是说这个方法只有定义，没有方法体
}
```

而具体的实现，需要由子类来完成，而且如果是子类，必须要实现抽象类中所有抽象方法：

```java
public class Worker extends Person{

    public Worker(String name, int age, String sex) {
        super(name, age, sex, "工人");
    }

    @Override
    public void exam() {   //子类必须要实现抽象类所有的抽象方法，这是强制要求的，否则会无法通过编译
        System.out.println("我是工人，做题我并不擅长，只能得到 D");
    }
}
```

抽象类由于不是具体的类定义（它是类的抽象）可能会存在某些方法没有实现，因此无法直接通过new关键字来直接创建对象：

![image-20220921231744420](https://s2.loli.net/2022/09/21/GLQU8hANw36P5J7.png)

要使用抽象类，我们只能去创建它的子类对象。

抽象类一般只用作继承使用，当然，抽象类的子类也可以是一个抽象类：

```java
public abstract class Student extends Person{   //如果抽象类的子类也是抽象类，那么可以不用实现父类中的抽象方法
    public Student(String name, int age, String sex) {
        super(name, age, sex, "学生");
    }

    @Override   //抽象类中并不是只能有抽象方法，抽象类中也可以有正常方法的实现
    public void exam() {
        System.out.println("我是学生，我就是小镇做题家，拿个 A 轻轻松松");
    }
}
```

注意，抽象方法的访问权限不能为`private`：

![image-20220921232435056](https://s2.loli.net/2022/09/21/1ZJSRU2Aj5K9Ikv.png)

因为抽象方法一定要由子类实现，如果子类都访问不了，那么还有什么意义呢？所以说不能为私有。

### 接口

接口甚至比抽象类还抽象，他只代表某个确切的功能！也就是只包含方法的定义，甚至都不是一个类！接口一般只代表某些功能的抽象，接口包含了一些列方法的定义，类可以实现这个接口，表示类支持接口代表的功能（类似于一个插件，只能作为一个附属功能加在主体上，同时具体实现还需要由主体来实现）

咋一看，这啥意思啊，什么叫支持接口代表的功能？实际上接口的目标就是将类所具有某些的行为抽象出来。

比如说，对于人类的不同子类，学生和老师来说，他们都具有学习这个能力，既然都有，那么我们就可以将学习这个能力，抽象成接口来进行使用，只要是实现这个接口的类，都有学习的能力：

```java
public interface Study {    //使用interface表示这是一个接口
    void study();    //接口中只能定义访问权限为public抽象方法，其中public和abstract关键字可以省略
}
```

我们可以让类实现这个接口：

```java
public class Student extends Person implements Study {   //使用implements关键字来实现接口
    public Student(String name, int age, String sex) {
        super(name, age, sex, "学生");
    }

    @Override
    public void study() {    //实现接口时，同样需要将接口中所有的抽象方法全部实现
        System.out.println("我会学习！");
    }
}
```

```java
public class Teacher extends Person implements Study {
    protected Teacher(String name, int age, String sex) {
        super(name, age, sex, "教师");
    }

    @Override
    public void study() {
        System.out.println("我会加倍学习！");
    }
}
```

接口不同于继承，接口可以同时实现多个：

```java
public class Student extends Person implements Study, A, B, C {  //多个接口的实现使用逗号隔开
  
}
```

所以说有些人说接口其实就是Java中的多继承，但是我个人认为这种说法是错的，实际上实现接口更像是一个类的功能列表，作为附加功能存在，一个类可以附加很多个功能，接口的使用和继承的概念有一定的出入，顶多说是多继承的一种替代方案。

接口跟抽象类一样，不能直接创建对象，但是我们也可以将接口实现类的对象以接口的形式去使用：

![image-20220921234735828](https://s2.loli.net/2022/09/21/VJfhzYKuF38tRq4.png)

当做接口使用时，只有接口中定义的方法和Object类的方法，无法使用类本身的方法和父类的方法。

接口同样支持向下转型：

```java
public static void main(String[] args) {
    Study study = new Teacher("小王", 27, "男");
    if(study instanceof Teacher) {   //直接判断引用的对象是不是Teacher类型
        Teacher teacher = (Teacher) study;   //强制类型转换
        teacher.study();
    }
}
```

这里的使用其实跟之前的父类是差不多的。

从Java8开始，接口中可以存在方法的默认实现：

```java
public interface Study {
    void study();

    default void test() {   //使用default关键字为接口中的方法添加默认实现
        System.out.println("我是默认实现");
    }
}
```

如果方法在接口中存在默认实现，那么实现类中不强制要求进行实现。

接口不同于类，接口中不允许存在成员变量和成员方法，但是可以存在静态变量和静态方法，在接口中定义的变量只能是：

```java
public interface Study {
    public static final int a = 10;   //接口中定义的静态变量只能是public static final的
  
  	public static void test(){    //接口中定义的静态方法也只能是public的
        System.out.println("我是静态方法");
    }
    
    void study();
}
```

跟普通的类一样，我们可以直接通过接口名.的方式使用静态内容：

```java
public static void main(String[] args) {
    System.out.println(Study.a);
    Study.test();
}
```

接口是可以继承自其他接口的：

```java
public interface A exetnds B {
  
}
```

并且接口没有继承数量限制，接口支持多继承：

```java
public interface A exetnds B, C, D {
  
}
```

接口的继承相当于是对接口功能的融合罢了。

最后我们来介绍一下Object类中提供的克隆方法，为啥要留到这里才来讲呢？因为它需要实现接口才可以使用：

```java
package java.lang;

public interface Cloneable {    //这个接口中什么都没定义
}
```

实现接口后，我们还需要将克隆方法的可见性提升一下，不然还用不了：

```java
public class Student extends Person implements Study, Cloneable {   //首先实现Cloneable接口，表示这个类具有克隆的功能
    public Student(String name, int age, String sex) {
        super(name, age, sex, "学生");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {   //提升clone方法的访问权限
        return super.clone();   //因为底层是C++实现，我们直接调用父类的实现就可以了
    }

    @Override
    public void study() {
        System.out.println("我会学习！");
    }
}
```

接着我们来尝试一下，看看是不是会得到一个一模一样的对象：

```java
public static void main(String[] args) throws CloneNotSupportedException {  //这里向上抛出一下异常，还没学异常，所以说照着写就行了
    Student student = new Student("小明", 18, "男");
    Student clone = (Student) student.clone();   //调用clone方法，得到一个克隆的对象
    System.out.println(student);
    System.out.println(clone);
    System.out.println(student == clone);
}
```

可以发现，原对象和克隆对象，是两个不同的对象，但是他们的各种属性都是完全一样的：

![image-20220922110044636](https://s2.loli.net/2022/09/22/E3dNFYT5sWaS8Rx.png)

通过实现接口，我们就可以很轻松地完成对象的克隆了，在我们之后的学习中，还会经常遇到接口的使用。

**注意：** 以下内容为选学内容，在设计模式篇视频教程中有详细介绍。

> 克隆操作可以完全复制一个对象的所有属性，但是像这样的拷贝操作其实也分为浅拷贝和深拷贝。
>
> * **浅拷贝：** 对于类中基本数据类型，会直接复制值给拷贝对象；对于引用类型，只会复制对象的地址，而实际上指向的还是原来的那个对象，拷贝个基莫。 
> * **深拷贝：** 无论是基本类型还是引用类型，深拷贝会将引用类型的所有内容，全部拷贝为一个新的对象，包括对象内部的所有成员变量，也会进行拷贝。
>
> 那么clone方法出来的克隆对象，是深拷贝的结果还是浅拷贝的结果呢？
>
> ```java
> public static void main(String[] args) throws CloneNotSupportedException {
>     Student student = new Student("小明", 18, "男");
>     Student clone = (Student) student.clone();
>     System.out.println(student.name == clone.name);
> }
> ```
>
> ![image-20220922110750697](https://s2.loli.net/2022/09/22/gpM1iukyoSdn2RE.png)
>
> 可以看到，虽然Student对象成功拷贝，但是其内层对象并没有进行拷贝，依然只是对象引用的复制，所以Java为我们提供的`clone`方法只会进行浅拷贝。

***

## 枚举类

假设现在我们想给小明添加一个状态（跑步、学习、睡觉），外部可以实时获取小明的状态：

```java
public class Student extends Person implements Study {

    private String status;   //状态，可以是跑步、学习、睡觉这三个之中的其中一种

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
```

但是这样会出现一个问题，如果我们仅仅是存储字符串，似乎外部可以不按照我们规则，传入一些其他的字符串。这显然是不够严谨的，有没有一种办法，能够更好地去实现这样的状态标记呢？我们希望开发者拿到使用的就是我们预先定义好的状态，所以，我们可以使用枚举类来完成：

```java
public enum Status {   //enum表示这是一个枚举类，枚举类的语法稍微有一些不一样
    RUNNING, STUDY, SLEEP;    //直接写每个状态的名字即可，最后面分号可以不打，但是推荐打上
}
```

使用枚举类也非常方便，就像使用普通类型那样：

```java
private Status status;   //类型变成刚刚定义的枚举类

public Status getStatus() {
    return status;
}

public void setStatus(Status status) {
    this.status = status;
}
```

这样，别人在使用时，就能很清楚地知道我们支持哪些了：

![image-20220922111426974](https://s2.loli.net/2022/09/22/6SDXckyIfFoCZWg.png)

枚举类型使用起来就非常方便了，其实枚举类型的本质就是一个普通的类，但是它继承自`Enum`类，我们定义的每一个状态其实就是一个`public static final`的Status类型成员变量：

```java
//这里使用javap命令对class文件进行反编译得到 Compiled from "Status.java"
public final class com.test.Status extends java.lang.Enum<com.test.Status> {
  public static final com.test.Status RUNNING;
  public static final com.test.Status STUDY;
  public static final com.test.Status SLEEP;
  public static com.test.Status[] values();
  public static com.test.Status valueOf(java.lang.String);
  static {};
}
```

既然枚举类型是普通的类，那么我们也可以给枚举类型添加独有的成员方法：

```java
public enum Status {
    RUNNING("睡觉"), STUDY("学习"), SLEEP("睡觉");   //无参构造方法被覆盖，创建枚举需要添加参数（本质就是调用的构造方法）

    private final String name;    //枚举的成员变量
    Status(String name){    //覆盖原有构造方法（默认private，只能内部使用！）
        this.name = name;
    }

    public String getName() {   //获取封装的成员变量
        return name;
    }
}
```

这样，枚举就可以按照我们想要的中文名称打印了：

```java
public static void main(String[] args) {
    Student student = new Student("小明", 18, "男");
    student.setStatus(Status.RUNNING);
    System.out.println(student.getStatus().getName());
}
```

枚举类还自带一些继承下来的实用方法，比如获取枚举类中的所有枚举，只不过这里用到了数组，我们会在下一章进行介绍。

至此，面向对象基础内容就全部讲解完成了，下一章我们还将继续讲解面向对象的其他内容。


————————————————
版权声明：本文为柏码知识库版权所有，禁止一切未经授权的转载、发布、出售等行为，违者将被追究法律责任。
原文链接：https://www.itbaima.cn/document/jviyz2hsht9ete5k?segment=2