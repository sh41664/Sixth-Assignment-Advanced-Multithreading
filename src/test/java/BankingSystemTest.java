import Banking.BankAccount;
import Banking.BankingMain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BankingSystemTest {
    private static BankingMain bankingMain;
    private static List<BankAccount> bankAccounts;
    @BeforeAll
    static void setup() throws InterruptedException {
        bankingMain = new BankingMain();
        bankAccounts = bankingMain.calculate();
    }

    @Test
    void testAccount1(){assertEquals(bankAccounts.get(0).getBalance(), 18900);
    }

    @Test
    void testAccount2(){assertEquals(bankAccounts.get(1).getBalance(), 27170);
    }

    @Test
    void testAccount3(){assertEquals(bankAccounts.get(2).getBalance(), 21400);
    }

    @Test
    void testAccount4(){assertEquals(bankAccounts.get(3).getBalance(), 10010);
    }

}