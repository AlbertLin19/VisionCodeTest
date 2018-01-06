import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public interface Pipeline {
		/**
		 * This is the primary method that runs the entire pipeline and updates the outputs.
		 */
		public void process(Mat source0);

		/**
		 * This method is a generated getter for the output of a Blur.
		 * @return Mat output from Blur.
		 */
		public Mat blurOutput();

		/**
		 * This method is a generated getter for the output of a HSV_Threshold.
		 * @return Mat output from HSV_Threshold.
		 */
		public Mat hsvThresholdOutput();

		/**
		 * This method is a generated getter for the output of a Find_Contours.
		 * @return ArrayList<MatOfPoint> output from Find_Contours.
		 */
		public ArrayList<MatOfPoint> findContoursOutput();

		/**
		 * This method is a generated getter for the output of a Filter_Contours.
		 * @return ArrayList<MatOfPoint> output from Filter_Contours.
		 */
		public ArrayList<MatOfPoint> filterContoursOutput();


		/**
		 * An indication of which type of filter to use for a blur.
		 * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
		 */
		enum BlurType{
			BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
				BILATERAL("Bilateral Filter");

			private final String label;

			BlurType(String label) {
				this.label = label;
			}

			public static BlurType get(String type) {
				if (BILATERAL.label.equals(type)) {
					return BILATERAL;
				}
				else if (GAUSSIAN.label.equals(type)) {
				return GAUSSIAN;
				}
				else if (MEDIAN.label.equals(type)) {
					return MEDIAN;
				}
				else {
					return BOX;
				}
			}

			@Override
			public String toString() {
				return this.label;
			}
		}
}
