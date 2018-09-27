import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;


public class DaisyAce {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Testing DaisyAce");
		
		//String command = "ace --outdir /home/codemantra/Documents/OAT/Ace /home/codemantra/Documents/OAT/the world broke in two_bill goldstein.epub";
		//String nvmCommand = "nvm exec daisy-ace ace --outdir /home/codemantra/Documents/OAT/Daisy-Ace /home/codemantra/Documents/OAT/the world broke in two_bill goldstein.epub";
		
			
		try {
			String[] cmd = new String[4];
	        cmd[0] = "ace";
			//cmd[0] = "/root/.nvm/versions/node/v8.12.0/bin/node";
	        cmd[1] = "--outdir";
	        cmd[2] = "/home/codemantra/Documents/OAT/Daisy-Ace";	        
	        cmd[3] = "/home/codemantra/Documents/OAT/9781628925494_ePub.epub";
	        
	        final Process process = Runtime.getRuntime().exec(cmd);
	        StreamConsumer stdout = new StreamConsumer(process.getInputStream());
	        
	        Thread inputReader = new Thread(stdout);
	        inputReader.start();
	        StreamConsumer stderr = new StreamConsumer(process.getErrorStream());
	        Thread stdErrReader = new Thread(stderr);
	        stdErrReader.start();
	        int exitCode = -1;
	        try {
	            exitCode = process.waitFor();
	        } catch (InterruptedException e) {
	            process.destroy();
	            inputReader.interrupt();
	            stdErrReader.interrupt();
	        } finally {
	            try {
	                inputReader.join();
	            } catch (InterruptedException e) {
	                // exit
	            }
	            try {
	                stdErrReader.join();
	            } catch (InterruptedException e) {
	                // exit
	            }
	        }
	        
	        System.out.println(exitCode);
	        
	        System.out.println(inputReader.toString());

	        String stderrResult = stderr.getResult();
	        if (stderrResult.length() > 0) {
	            System.out.println("Epub Accessiblilty Checker: " + stderrResult);
	        }
	        
	        

	        
	        
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
    private static class StreamConsumer implements Runnable {
        private final InputStream stream;
        private StringBuilder logMessage = new StringBuilder(4096);

        public StreamConsumer(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            int count;
            try {
                while ((count = stream.read(buffer)) > 0) {
                    logMessage.append(new String(buffer, 0, count));
                    if (Thread.interrupted()) {
                        return;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Can't read");
            } finally {
                IOUtils.closeQuietly(stream);
            }

        }

        public String getResult() {
            return logMessage.toString();
        }
    }
	

}