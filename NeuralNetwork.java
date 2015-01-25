import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class NeuralNetwork {
	private double[][] features;
	private double[] weights;
	private int M;
	private int N;
	private double[] median;
	int[] intLabel;
	private static final int epochs = 1;
	private static final double alpha = .1;
	private static final String neg = "-1";
	private Scanner in;
	public NeuralNetwork(Scanner in) {
		this.in = in;
		M = in.nextInt();
		N = in.nextInt();
		features = new double[M][N];
		weights = new double[N];
		intLabel = new int[M];
		double[][] rotate = new double[N][M];
		for (int i = 0; i < features.length; i++) {
			in.next();
			String temp = in.next();
			if (temp.equals(neg)) {
				intLabel[i] = -1;
			}
			else {
				intLabel[i] = 1;
			}
			for (int j = 0; j < features[i].length; j++) {
				String[] a = in.next().split(":");
				features[i][j] = (Double.parseDouble(a[1]));
			}
		}
		for (int i = 0; i < features.length; i++) {
			for (int j = 0; j < features[i].length; j++) {
				rotate[j][i] = features[i][j];
			}
		}
		for (int i = 0; i < rotate.length; i++) {
			Arrays.sort(rotate[i]);
		}
		median = new double[N];
		for (int i = 0; i < median.length; i++) {
			median[i] = rotate[i][M / 2];
		}
		for (int i = 0; i < features.length; i++) {
			for (int j = 0; j < features[i].length; j++) {
				if (features[i][j] > median[j]) {
					features[i][j] = 1;
				}
				else {
					features[i][j] = -1;
				}
			}
		}
		updateWeights();
	}
	public void updateWeights() {
		Random random = new Random();
		double[] activation = new double[N];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble() * 2 - 1;
		}
		//run for "epochs" number of rounds
		for (int k = 0; k < epochs; k++) {
			//iterate through each example
			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					activation[j] = features[i][j];
				}
				double input = 0.0;
				//get the weighted sum
				for (int j = 0; j < N; j++) {
					input += weights[j] * activation[j];
		
				}
				double expected = sigmoid(input);
				//compute the deltas as given in R&N
				double delta = sigmoid(input) * (1 - sigmoid(input)) *
						((double) intLabel[i] - expected);

				//update weights as given in algorithm of R&N
				for (int j = 0; j < N; j++) {
					weights[j] += alpha * activation[j] * delta;
				}
			}
		}
	}
	public void predict() {
		/*for (int i = 0; i < weights.length; i++) {
			System.out.print(weights[i] + " ");
		}
		System.out.println();*/
		int cases = in.nextInt();
		for (int i = 0; i < cases; i++) {
			System.out.print(in.next() + " ");
			double[] example = new double[N];
			for (int j = 0; j < N; j++) {
				String[] temp = in.next().split(":");
				example[j] = (Double.parseDouble(temp[1]));
				if (example[j] > median[j]) {
					example[j] = 1;
				}
				else {
					example[j] = -1;
				}
			}
			System.out.println(evaluate(example));
		}
	}
	private static double sigmoid(double x) {
		double exp = Math.exp(x * -1);
		return 1 / (1 + exp);
	}
	public String evaluate(double[] example) {
		double evaluate = 0;
		for (int i = 0; i < example.length; i++) {
			evaluate += weights[i] * example[i];
		}
		if (evaluate > -3) {
			return "+1";
		}
		return "-1";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		NeuralNetwork nn = new NeuralNetwork(in);
		nn.predict();
		// TODO Auto-generated method stub

	}

}
