import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Customer
{
    String custName;
    int custLoan;
    Customer(String name, int loanAmount)
    {
        this.custName=name;
        this.custLoan=loanAmount;
    }
    public String getName()
    {
        return custName;
    }
    public int getAmount()
    {
        return custLoan;
    }
}
public class money implements Runnable {
    private static HashMap<String, Integer> custMap = new HashMap<String, Integer>();
    private static HashMap<String, Integer> bankMap = new HashMap<String, Integer>();

    public void readCustomer() throws FileNotFoundException, IOException {
        String line1;
        BufferedReader reader = new BufferedReader(new FileReader("/Users/gursimratkaur/BankSystem/src/com/company/customers.txt"));
        System.out.println("*** Customer and loan objectives***");
        while ((line1=reader.readLine()) != null)
        {
            line1 = line1.replaceAll("\\}", "");
            line1 = line1.replaceAll("\\{", "");
            line1 = line1.replaceAll("\\.", "");
            String[] parts=line1.split(",",2);
            if(parts.length>=2)
            {
                String key=parts[0];
                String value=parts[1];
                System.out.println(key+":"+value);
                custMap.put(key,Integer.parseInt(value));
            }
        }

    }

    public void readBank() throws FileNotFoundException, IOException {
        String line2;
        BufferedReader reader = new BufferedReader(new FileReader("/Users/gursimratkaur/BankSystem/src/com/company/banks.txt"));
        System.out.println("*** Banks and financial resources***");
        while ((line2=reader.readLine()) != null)
        {
            line2 = line2.replaceAll("\\}", "");
            line2 = line2.replaceAll("\\{", "");
            line2 = line2.replaceAll("\\.", "");
            String[] parts=line2.split(",",2);
            if(parts.length>=2)
            {
                String key=parts[0];
                String value=parts[1];
                System.out.println(key+":"+value);
                bankMap.put(key,Integer.parseInt(value));
            }

        }
    }
   public void start()
   {


   }

    public synchronized void run()
    {
        try
        {
            while(true)
            {
                Random rand=new Random();
                List<String> keys1=new ArrayList<>(custMap.keySet());
                String name=keys1.get(rand.nextInt(keys1.size()));
                int loanAmt=custMap.get(name);
                Customer customer=new Customer(name,loanAmt);
               int borrowMoney=(int)(Math.random()* 50+1);
                if(customer.getAmount()==0)
                {
                    break;
                }
//               if(customer.getAmount()-borrowMoney<0)
//               {
//                   borrowMoney=(int)(Math.random()*customer.getAmount());
//               }
               Random random=new Random();
               List<String> keys2=new ArrayList<>(bankMap.keySet());
               String randomBank=keys2.get(random.nextInt(keys2.size()));
               int bankResource=bankMap.get(randomBank);
               System.out.println(customer.getName()+" request loan of "+borrowMoney+" from bank "+randomBank);
               if(bankResource>borrowMoney)
               {
                   bankResource=bankResource-borrowMoney;
                   System.out.println(randomBank+" approves loan of "+borrowMoney +" from "+customer.getName());
                   loanAmt=customer.getAmount()-borrowMoney;
                   custMap.put(name,loanAmt);
                   bankMap.put(randomBank,bankResource);
               }
               else
               {
                   bankMap.remove(randomBank);
                   System.out.println(randomBank+" denies a loan of " +borrowMoney +" from "+customer.getName());
               }

            }
        }
        finally {
            System.out.println("no");
        }

    }

    public static void main(String... a) throws IOException {
        money obj=new money();
        obj.readCustomer();
        obj.readBank();
        ExecutorService executor=Executors.newCachedThreadPool();
        for(int i=0;i<custMap.size();i++)
        {
            executor.execute(obj);

        }


    }
}
