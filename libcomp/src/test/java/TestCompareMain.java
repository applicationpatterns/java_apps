import org.ap.libcomp.CompareMain;
import org.junit.jupiter.api.Test;

// this is a library, so I don't think I really need a main
// get rid of this eventually
public class TestCompareMain {
    @Test
    void testCompareMain() {
        CompareMain cm = new CompareMain();
        CompareMain.main(null);
    }
}
