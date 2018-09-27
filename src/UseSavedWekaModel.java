import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class UseSavedWekaModel {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		ConverterUtils.DataSource source2 = new ConverterUtils.DataSource("test.arff");
        Instances test = source2.getDataSet();
        
        if (test.classIndex() == -1)
            test.setClassIndex(test.numAttributes() - 1);
        
        Classifier cls = (Classifier) weka.core.SerializationHelper.read("/home/codemantra/Documents/OAT/naiveBayes.model");

        double label = cls.classifyInstance(test.instance(0));
        test.instance(0).setClassValue(label);

        System.out.println(test.instance(0).stringValue(16));
	}

}
