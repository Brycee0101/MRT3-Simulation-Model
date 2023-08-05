
/**
 *
 * @author bryce stephen halnin
 */
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;

public class mrtmodel {
    
    static int headway;
    static int totTrains;
    
    static int simDuration = 14400;
    static int gateMax = 30;
    static int trainCapacity = 1182;
    static int loadingCapacity = 394;
    static int stationCapacity = 360;
    static int dwellTime = 35;
    static int[] activeTrains;
    static int[] trainPos;
    static int[] stationPax = new int[26];
    static int[] stationArrRate = new int[26];
    static int[] stationExRate = new int[26];
    static int minCounter = 0;
    static int trainCounter = 1;

    static String[] stationNames = new String[26];
    //northAveNorthBound
    static int northAveNorth = 0;
    static int stationGateWait = 0;
    static int stationGateBuffer;
    static int northAveArrRate = 15;
    
    static boolean overcrowd = false;
    
    public static void main(String[] args) {
        initializeStations();
         
        askInput();

        initializeTrains();
        
        doubleData();
        
        for(int i = 0; i < simDuration; i++){
            int count = 0;
            count++;
            
            if(i % 60 == 0){
                minCounter++;
                //gate entry logic
                for(int j = 0; j < stationArrRate.length;j++){
                    stationGateBuffer = 0;
                    stationGateWait += stationArrRate[j];
                    stationGateBuffer += Math.min(stationGateWait, gateMax);
                        if(Math.min(stationGateBuffer, gateMax) == gateMax){
                            stationGateWait = stationGateWait - gateMax;
                        }else{
                            stationGateWait = 0;
                        }
                    stationPax[j] += Math.min(stationGateBuffer, gateMax);
                        if(Math.min(stationGateBuffer, gateMax)== gateMax){
                            stationGateWait += stationGateBuffer - gateMax;
                        }
                        if(stationPax[j] > stationCapacity){
                            overcrowd = true;
                            System.out.println("Overcrowd Instance at " + stationNames[j] + " occurred");
                        }
                }
            }
            
                    
            //train arriving logic
            if(i % headway == 0){
                for(int k = 0; k < totTrains; k++){
                    int stationIndex = trainPos[k];
                    int remainingCapacity = trainCapacity - activeTrains[k];
                    int paxBoarded = 0;
                    
//                    System.out.println("Train " + k + " is at " + stationNames[stationIndex]);
                    //board pax
                        if(remainingCapacity > 0){
                            paxBoarded = Math.min(stationPax[stationIndex],Math.min(loadingCapacity, remainingCapacity));
                            activeTrains[k] += paxBoarded;
                            stationPax[stationIndex] -= Math.min(stationPax[stationIndex],loadingCapacity);
                        }
                    //detrain pax
                        if(activeTrains[k] > stationExRate[stationIndex]){
                            activeTrains[k] -= stationExRate[stationIndex];
                        }
                        
                        if(activeTrains[k] > trainCapacity || stationExRate[k] > (stationCapacity - stationPax[k])){
                            overcrowd = true;
                            System.out.println("Overcrowd Instance at " + stationNames[k] + " occurred");
                        }
                        
                    trainPos[k] = (stationIndex + 1) % stationNames.length;
                    System.out.println("Train " + k + " boards " + paxBoarded);
                    System.out.println("Train " + k + " carries " + activeTrains[k] + " passengers");
                    System.out.println("Train " + k + " departs for " + stationNames[trainPos[k]]);
                    System.out.println(" ");
                }
            }
        }
            if(overcrowd){
                System.out.println("Overcrowding Occured during the Simulation");
            }else{
                System.out.println("No Overcrowding occured during the Simulation.");
            }
    }
    
    static void initializeStations(){
        stationNames[0] = "North Ave Southbound";
        stationNames[1] = "Quezon Ave Southbound";
        stationNames[2] = "GMA Kamuning Southbound";
        stationNames[3] = "Cubao Southbound";
        stationNames[4] = "Santolan Southbound";
        stationNames[5] = "Ortigas Southbound";
        stationNames[6] = "Shaw Southbound";
        stationNames[7] = "Boni Southbound";
        stationNames[8] = "Guadalupe Southbound";
        stationNames[9] = "Buendia Southbound";
        stationNames[10] = "Ayala Southbound";
        stationNames[11] = "Magallaness Southbound";
        stationNames[12] = "Taft Ave Southbound";
        stationNames[13] = "Taft Ave Northbound";
        stationNames[14] = "Magallanes Northbound";
        stationNames[15] = "Ayala Northbound";
        stationNames[16] = "Buendia Northbound";
        stationNames[17] = "Guadalupe Northbound";
        stationNames[18] = "Boni Northbound";
        stationNames[19] = "Shaw Northbound";
        stationNames[20] = "Ortigas Northbound";
        stationNames[21] = "Santolan Northbound";
        stationNames[22] = "Cubao Northbound";
        stationNames[23] = "GMA Kamuning Northbound";
        stationNames[24] = "Quezon Ave Northbound";
        stationNames[25] = "North Ave Northbound";
        
        stationArrRate[0] = 15; stationArrRate[25] = 15;  
        stationArrRate[1] = 7;  stationArrRate[24] = 7; 
        stationArrRate[2] = 3;  stationArrRate[23] = 3; 
        stationArrRate[3] = 8;  stationArrRate[22] = 8; 
        stationArrRate[4] = 2;  stationArrRate[21] = 2; 
        stationArrRate[5] = 4;  stationArrRate[20] = 4; 
        stationArrRate[6] = 7;  stationArrRate[19] = 7; 
        stationArrRate[7] = 5;  stationArrRate[18] = 5;
        stationArrRate[8] = 6;  stationArrRate[17] = 6;
        stationArrRate[9] = 3;  stationArrRate[16] = 3; 
        stationArrRate[10] = 7;  stationArrRate[15] = 7;
        stationArrRate[11] = 7;  stationArrRate[14] = 7;
        stationArrRate[12] = 16;  stationArrRate[13] = 16; 
        
        stationExRate[0] = 9; stationExRate[25] = 9;  
        stationExRate[1] = 6;  stationExRate[24] = 6; 
        stationExRate[2] = 2;  stationExRate[23] = 2; 
        stationExRate[3] = 11;  stationExRate[22] = 11; 
        stationExRate[4] = 2;  stationExRate[21] = 2; 
        stationExRate[5] = 6;  stationExRate[20] = 6; 
        stationExRate[6] = 10;  stationExRate[19] = 10; 
        stationExRate[7] = 5;  stationExRate[18] = 5;
        stationExRate[8] = 7;  stationExRate[17] = 7;
        stationExRate[9] = 3;  stationExRate[16] = 3; 
        stationExRate[10] = 8; stationExRate[15] = 8;
        stationExRate[11] = 4; stationExRate[14] = 4;
        stationExRate[12] = 13;stationExRate[13] = 13; 
    }
    
    static void initializeTrains(){
        activeTrains = new int[totTrains];
        Arrays.fill(activeTrains, 0);
        trainPos = new int[totTrains];
        for(int i = 0; i < totTrains; i++){
            trainPos[i] = (i * stationNames.length)/totTrains;
        }
        for(int i = 0; i < totTrains; i++){
            activeTrains[i] = (trainCapacity/4)*3;
        }
        
        for(int i=0; i<stationPax.length;i++){
            stationPax[i] = stationCapacity/2;
        }
    }
    
    static void askInput(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Headway (in minutes): ");
            headway = scanner.nextInt();
            headway *= 60;
//        while(totTrains > 2 || totTrains < 19){
            System.out.print("Enter Number of Active Trains: ");
            totTrains = scanner.nextInt();
//        }
    }
    
    static void doubleData(){
        for(int i = 0; i < stationArrRate.length; i++){
            stationArrRate[i] *= 2;
        }
    }
    
    
}


