package com.securevault.temp;


@FunctionalInterface
interface MathOperation {
    int add(int a, int b);

    default int subtract(int a, int b) {
        return a - b;
    }

    static int multiply(int a, int b) {
        return a * b;
    }


}

public class LambdaExpression {

    int c = 10;
    int a = 4;
    public static void main(String[] args) throws InterruptedException {


    }

    public void test() {
        MathOperation m2 = new MathOperation() {
            int a = 10;
            @Override
            public int add(int x, int y) {
                a++;
                c = c + 1;
                System.out.println(this.a);
                return 0;
            }
        };

        m2.add(1, 2);}
}

class TestImpl implements MathOperation {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a + b;
    }

}
