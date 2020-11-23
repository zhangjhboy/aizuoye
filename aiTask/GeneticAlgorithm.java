import java.util.Random;

public class GeneticAlgorithm {
    private final double pi = 3.14159265;
    private final double pcross = 0.7;
    private final double pmutate = 0.001;
    private final int len = 22;
    private final int algebra = 500;
    private final int size = 500;
    Node bestChromo = new Node();
    Node[] group = new Node[size];
    Node[] temp = new Node[size];

    public GeneticAlgorithm() {
        for (int i = 0; i < size; i++) {
            group[i] = new Node();
            temp[i] = new Node();
        }
    }

    private void structrue(Node c) {
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            c.chromo[i] = random.nextInt(2) == 0 ? false : true;
        }
    }

    private double decode(Node c) {
        double num = 4194394;
        double tem = 0;
        for (int i = 0; i < len; i++) {
            double ch = c.chromo[i] ? 1 : 0;
            tem += ch * Math.pow(2, i);
        }
        return (3 / num * tem) - 1;
    }

    private double f(double x) {
        return x * Math.sin(10 * pi * x) + 2.0;
    }

    private double fitness(Node c) {
        double x = decode(c);
        return f(x);
    }

    private void cross(Node c1, Node c2, int point) {
        Node c3 = new Node(c1);
        for (int i = 0; i < len - point; i++) {
            c1.chromo[point + i] = c2.chromo[point + i];
        }
        for (int j = 0; j < len - point; j++) {
            c2.chromo[point + j] = c3.chromo[point + j];
        }
    }

    private void mutate(Node c) {
        Random random = new Random();
        int i = random.nextInt(len);
        c.chromo[i] = !c.chromo[i];
    }

    private void select(Node[] group) {
        double[] fitnessVal = new double[size];
        double sum = 0;
        double[] avgFitness = new double[size];
        int[] id = new int[size];
        for (int i = 0; i < size; i++) {
            fitnessVal[i] = fitness(group[i]);
        }
        for (int i = 0; i < size; i++) {
            sum += fitnessVal[i];
        }
        for (int i = 0; i < size; i++) {
            avgFitness[i] = fitnessVal[i] / sum;
        }
        for (int i = 1; i < size; i++) {
            avgFitness[i] += avgFitness[i - 1];
        }
        for (int i = 0; i < size; i++) {
            double rannum = Math.random();
            int j;
            for (j = 0; j < size - 1; j++) {
                if (rannum < avgFitness[j]) {
                    id[i] = j;
                    break;
                }
            }
            if (j == size - 1) {
                id[i] = j;
            }
        }
        for (int i = 0; i < size; i++) {
            temp[i] = new Node(group[i]);
        }
        for (int i = 0; i < size; i++) {
            group[i] = new Node(temp[id[i]]);
        }
    }

    private ReturnValue getBest(Node[] group) {
        double[] fitnessVal = new double[size];
        for (int i = 0; i < size; i++) {
            fitnessVal[i] = fitness(group[i]);
        }
        int id = 0;
        for (int i = 1; i < size; i++) {
            if (fitnessVal[i] > fitnessVal[id]) {
                id = i;
            }
        }
        ReturnValue returnValue = new ReturnValue();
        returnValue.x = decode(group[id]);
        returnValue.number = f(returnValue.x);
        returnValue.id = id;
        return returnValue;
    }

    public void GA() {
        for (int i = 0; i < size; i++) {
            structrue(group[i]);
        }
        ReturnValue returnValue = getBest(group);
        double x = returnValue.x;
        bestChromo = group[returnValue.id];
        for (int i = 0; i < algebra; i++) {
            Random random = new Random();
            select(group);
            int p = random.nextInt(len);
            for (int j = 0, pre = -1; j < size; j++) {
                if (Math.random() < pcross) {
                    if (pre == -1)
                        pre = j;
                    else {
                        cross(group[pre], group[j], p);
                        pre = -1;
                    }
                }
            }
            for (int k = 0; k < size; k++) {
                if (Math.random() < pmutate) {
                    mutate(group[k]);
                }
            }
            ReturnValue returnValue2 = getBest(group);
            x = returnValue2.x;
            //double number = returnValue2.number;
            System.out.println("第" + i + "代最优x值为：" + x + "函数值为：" + f(x));
        }
    }

    class Node {
        public boolean[] chromo = new boolean[len];
        public Node() {}
        public Node(Node n) {
            for (int i = 0; i < len; i++) {
                chromo[i] = n.chromo[i];
            }
        }
    }
    class ReturnValue {
        public double x;
        public double number;
        public int id;
    }

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
	    geneticAlgorithm.GA();
    }
    
}