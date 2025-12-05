package InetAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class InetAddressTest {
    public static void main(String[] args) throws IOException {
        String targetHost;
        if (args.length == 0) {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter a domain name: ");
            targetHost = in.nextLine();
        } else targetHost = args[0];

        InetAddress[] addresses = InetAddress.getAllByName(targetHost);
        System.out.println("All address(es) of domain %s:".formatted(targetHost));
        for (InetAddress address : addresses) System.out.println(address);
    }
}
