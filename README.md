# HCIRA_Project1_Part3
Project Requirements:
a) read in a gesture dataset from files to use for templates and candidates; 
TrainandTest.java main function Ln 77, Col 38 makes a call for the ìMakeGestureî method wherein at Ln 23, Col 48 call for the ìReadî method is made. This is the method where the log file is being read.
b) connect to your existing $1 pre-processing and recognition methods; 
TrainandTest.java main function Ln 108, Col 42 calls the ìRecognizeî method which is updated a little for this submission requirement.
c) loop over the gesture dataset to systematically configure your recognizer and test it; and 
TrainandTest.java main function Ln 101, Col 29, a ìforî loop starts to iterate over the gesture dataset for testing.
d) output the result of the recognition tests to a log file.
Every iteration log is added to the log file and the code can be found at TrainandTest.java main function Ln 112 to Ln 166.
TrainandTest.java main function Ln 171, Col 21 logs the user-wise accuracy on the terminal window, and TrainandTest.java main function Ln 173, Col 17 logs the Total Average Accuracy at the bottom of the log file.

