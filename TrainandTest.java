import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TrainandTest {
    public static void main(String[] args) {
        String user = "";
        String gestureType = "";
        ArrayList<ArrayList<CustomReturnType>> userData = new ArrayList<>();

        // to store the time taken for training
        double[] timePerTrainingSamples = new double[8];

        // creatung the log file
        try {
            File file = new File("HCIRA-Proj1-logfile.csv");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileOutputStream writer = new FileOutputStream("HCIRA-Proj1-logfile.csv", false);
                writer.write(("").getBytes());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            try (FileOutputStream logger = new FileOutputStream("HCIRA-Proj1-logfile.csv", true)) {
                // writing to log file
                logger.write(
                        ("Recognition Log: [Harshwardhan Chauhan] // [$1 Recognizer] // [Dataset] // USER-DEPENDENT RANDOM-10,,,,,,,,,,,\n")
                                .getBytes());

                //writing headers to log file
                logger.write(
                        ("User[all-users],GestureType[all-gestures-types],RandomIteration[1to10],#ofTrainingExamples[E],TotalSizeOfTrainingSet[count],TrainingSetContents[specific-gesture-instances],Candidate[specific-instance],RecoResultGestureType[what-was-recognized],CorrectIncorrect[1or0],RecoResultScore,RecoResultBestMatch[specific-instance],RecoResultNBestSorted[instance-and-score]\n")
                                .getBytes());


                double totalResult = 0;
                double totalCorrect = 0;
                // iterate for every user
                for (int userID = 2; userID <= 11; userID++) {
                    double userResult = 0;
                    double userTotal = 0;
                    if (userID < 10) {
                        user = "s" + "0" + userID;
                    } else {
                        user = "s" + userID;
                    }

                    String GestureType[] = new String[] {
                        "arrow",
                        "caret",
                        "check",
                        "circle",
                        "delete_mark",
                        "left_curly_brace",
                        "left_sq_bracket",
                        "pigtail",
                        "question_mark",
                        "rectangle",
                        "right_curly_brace",
                        "right_sq_bracket",
                        "star",
                        "triangle",
                        "v",
                        "x"
                    };

                    for (int gestures = 0; gestures < 16; gestures++) {
                        gestureType = GestureType[gestures];
                        userData.add(Helper.MakeGesture(user, gestureType));
                    }

                    // iterate through training sample size
                    for (int i = 0; i < 10; i++) {
                        for (int trainingSampleSize = 1; trainingSampleSize <=9; trainingSampleSize++) {
                            for (int l = 0; l < userData.size(); l++) {
                                Collections.shuffle(userData.get(l)); //shuffle the user data
                            }

                            ArrayList<ArrayList<CustomReturnType>> trainingSet = new ArrayList<>();

                            // iterate through each gesture
                            for (int gestures = 0; gestures < 16; gestures++) {
                                ArrayList<CustomReturnType> trainedGestures = new ArrayList<>();

                                // fill in the trainedGestures with samples
                                for (int trainingSampleNumber = 0; trainingSampleNumber < trainingSampleSize; trainingSampleNumber++) {
                                    trainedGestures.add(userData.get(gestures).get(trainingSampleNumber));
                                }

                                trainingSet.add(trainedGestures);
                            }

                            // iterate through the gestures
                            for (int gestures = 0; gestures < 16; gestures++) {
                                //Timer tempTimer = new Timer();

                                // get n best list by passing the training set and gesture to be tested
                                DollarOneRecognizer dollarOneRecognizer = new DollarOneRecognizer();
                                ArrayList<CustomReturnType> nbestList = dollarOneRecognizer
                                        .Recognize(userData.get(gestures).get(9), trainingSet);

                                // writing to the log
                                //timePerTrainingSamples[trainingSampleSize - 2] += tempTimer.getTime();

                                logger.write((user + ",").getBytes());

                                logger.write((GestureType[gestures] + ",").getBytes());

                                logger.write((i + 1 + ",").getBytes());

                                logger.write((trainingSampleSize + ",").getBytes());

                                logger.write(((trainingSampleSize) * 16 + ",").getBytes());

                                // logging the training set data
                                logger.write(("\"{").getBytes());
                                for (int m = 0; m < trainingSet.size(); m++) {
                                    for (int n = 0; n < trainingSet.get(m).size(); n++) {
                                        if(m==trainingSet.size()-1 && n==trainingSet.get(m).size()-1)
                                        logger.write((user + "-" + userData.get(m).get(n).gesture + "-"
                                                + userData.get(m).get(n).gestureNumber).getBytes());
                                        else
                                        logger.write((user + "-" + userData.get(m).get(n).gesture + "-"
                                        + userData.get(m).get(n).gestureNumber + ",").getBytes());
                                    }
                                }
                                logger.write(("}\""+",").getBytes());

                                logger.write((user + "-" + userData.get(gestures).get(9).gesture + "-"
                                        + userData.get(gestures).get(9).gestureNumber + ",").getBytes());

                                // best match
                                logger.write((nbestList.get(0).gesture + ",").getBytes());

                                // log result of the recognition
                                if (nbestList.get(0).gesture.equals(userData.get(gestures).get(9).gesture)) {
                                    logger.write((1 + ",").getBytes());
                                    userResult++;
                                    totalCorrect++;
                                } else {
                                    logger.write((0 + ",").getBytes());
                                }
                                totalResult++;
                                userTotal++;

                                // log best match score
                                logger.write((nbestList.get(0).score + ",").getBytes());

                                // log best match instance
                                logger.write((user + "-" + nbestList.get(0).gesture + "-"
                                        + nbestList.get(0).gestureNumber + ",").getBytes());

                                // log nbestlist
                                logger.write(("\"{").getBytes());
                                for (int m = 0; m < nbestList.size()-1; m++) {
                                    logger.write((user + "-" + nbestList.get(m).gesture + "-"
                                    + nbestList.get(m).gestureNumber + "," + nbestList.get(m).score+",").getBytes());
                                    if (m > 51) {
                                       break;

                                    }
                                }
                                logger.write((user + "-" + nbestList.get(nbestList.size()-1).gesture + "-"
                                    + nbestList.get(nbestList.size()-1).gestureNumber + "," + nbestList.get(nbestList.size()-1).score).getBytes());
                                logger.write(("}\"").getBytes());
                                logger.write(("\n").getBytes());
                            }

                        }
                    }
                    System.out.println("User "+ (userID-1)+" Accuracy: "+(userResult/userTotal));
                }
                logger.write(("TotalAvgAccuracy: "+(totalCorrect/totalResult)).getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Log File Generated.");
    }
}
