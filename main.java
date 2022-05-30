import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class main {
    public static void main(String[] args) throws FileNotFoundException {
        //------------------------------------variables----------------------//
        //measure time
        double starttime = System.currentTimeMillis();
        //read file
        System.out.println("--");
        File f = new File("updates.20150612.0845-0945.txt");
        Scanner myscan = new Scanner(f);
        //Q1
        long counter = 0;
        //Q2
        long num_announcements = 0;
        //Q3
        long num_withdrawals = 0;
        //Q4
        HashSet<String> myset = new HashSet<String>();
        //Q5
        HashSet<String> myset2 = new HashSet<String>();
        //Q6
        HashSet<String> myset3 = new HashSet<String>();
        //Q7
        long burstcount = 0;
        HashMap<String, ArrayList<Long>> bursts = new HashMap<>();

        //Q8
        int max = 0;
        //Q9
        long longest = 0;
        //Q10
        double longestburstavtotal = 0;
        //Q11
        long zerobursts = 0;
        //Q12
        int longerthan10 = 0;
        //Q13
        int longerthan20 = 0;
        //Q14
        int longerthan30 = 0;
        //debug
        int counter2 = 0;

        //------------------------------------------------main-loop------------------------------------------//
        while(myscan.hasNextLine()){
            //put input into String[]
            String thisline = myscan.nextLine();
            String[] thislinearr = new String[14];
            
            thislinearr = thisline.split("\\|");

            //status updater (debug purposes)
            /*
            if(counter2 == 100000){
                System.out.println(counter/100000);
                //System.out.println( " "+burstcount);
                counter2 = 0;
                
            }
            */
            //Q2 & 5
            if(thislinearr[2].equals("A")){
                num_announcements++;
                myset2.add(thislinearr[5]);
            }
            else{
                //Q3 & 6
                num_withdrawals++;
                myset3.add(thislinearr[5]);
            }
            
            //Q4
            if(thislinearr.length>=6){
                myset.add(thislinearr[5]);
            }

            //Q7 - detect bursts

            //if prefix not yet contained in prefixes
            if(!bursts.containsKey(thislinearr[5])){
                String temp = thislinearr[1];
                ArrayList<Long> curr = new ArrayList<>();
                curr.add(Long.parseLong(temp));
                bursts.put(thislinearr[5], curr);
            }
            //prefix already in bursts; add new time to list
            else{
                bursts.get(thislinearr[5]).add(Long.parseLong(thislinearr[1]));

            }
            //Q1 & debug purposes
            //counter2++;
            counter++;
        }

        //---------------------------------------------------after loop calculations----------------------------------------------//
        //count number of bursts (Q7)
        for(Map.Entry<String, ArrayList<Long>> i : bursts.entrySet()){
            ArrayList<Long> analyse = i.getValue();
            //count number of bursts of each prefix
            long t0 = 0;
            long tminus1 = 0;

            int k = 0;
            double numbursts = 0;
            int maxburst = 0;
            long longestburst = 0;
            
            long thislongestburst = 0;
            double thisburstsum = 0;
            
            if (analyse.size()<= 1){
                //do nothing (no burst)
            }
            else{
                //count bursts
                for(long j : analyse){
                    //read first timestamp
                    if (k==0){
                        t0 = j;
                        k++;
                        continue;
                    }
                    //read second timestamp
                    if(k==1){
                        tminus1 = t0;
                        t0 = j;
                        k++;
                        if(j-tminus1 < 240){
                            //new burst (first)
                            thislongestburst = j-tminus1;
                            thisburstsum += j-tminus1;

                            burstcount++;
                            numbursts++;
                            maxburst++;
                        }
                        continue;
                    }
                    
                    
                    if (j - t0 < 240 && t0-tminus1 < 240){
                        //not a new burst
                        thislongestburst += j-t0;
                        thisburstsum += j-t0;
                    }
                    else if(j-t0 < 240 && t0 - tminus1 >= 240){
                        //new burst
                        if(thislongestburst>longestburst){
                            longestburst = thislongestburst;
                        }
                        thislongestburst = j-t0;
                        thisburstsum += j-t0;
                        
                        burstcount++;
                        numbursts++;
                        maxburst++;
                    }
                    //update
                    tminus1 = t0;
                    t0 = j;
                }

            }
            if(thislongestburst>longestburst){
                longestburst = thislongestburst;
            }
            
            //new maxburst detected
            if (maxburst>max){
                max = maxburst;
            }
            //new longest burst detected
            if(longestburst > longest){
                longest = longestburst;
            }
            //longestburstavtotal
            longestburstavtotal += longestburst;

            //no bursts at all
            if(numbursts == 0){
                zerobursts++;
            }


            //longer than 10 minutes
            double divis = Math.ceil(thisburstsum/numbursts);
            
            if(numbursts != 0 && divis > 600){
                longerthan10++;
            }
            //longer than 20 minutes
            if(numbursts != 0 && divis > 1200){
                longerthan20++;
            }
            //longer than 30 minutes
            if(numbursts != 0 && divis > 1800){
                longerthan30++;
            }
        }
        
    
    
//----------------------------------------------print result --------------------------------------------------------//

        long result[] = new long[14];
        //number of all update messages
        result[0] = counter;
        //number of announcements
        result[1] = num_announcements;
        //number of withdrawals
        result[2] = num_withdrawals;
        //number of prefixes
        result[3] = myset.size();
        //number of prefixes with at least an annoucement
        result[4] = myset2.size();
        //number of prefixes with at least an withdrawal
        result[5] = myset3.size();
         //number of bursts 
        result[6] = burstcount;
        
        //max burst
        result[7] = max;
        //longest burst
        result[8] = longest;
        //longest burst av total !!off by one
        double burstsize = bursts.size();
        double thisresult = longestburstavtotal / burstsize;
        result[9] = (long)Math.ceil(thisresult);
        //no bursts at all 
        result[10] = zerobursts;
        //longer than 10 minutes !!off by one
        result[11] = longerthan10;
        //longer than 20 minutes
        result[12] = longerthan20;
        //longer than 30 minutes
        result[13] = longerthan30;

        for(int i = 0; i<result.length; i++){
            System.out.println(i+1+": "+result[i]);
        }

        //time measurement
        double endtime = System.currentTimeMillis();
        System.out.println((endtime-starttime) / 1000);

    }

    
}
