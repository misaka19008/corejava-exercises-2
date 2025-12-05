package MemoryMap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.zip.CRC32;

public class MemoryMapTest {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter the file path > ");
        Path filename = Path.of(new Scanner(System.in).next());
        String crcValue = "";
        long start = 0;
        long end = 0;

        start = System.currentTimeMillis();
        crcValue = Long.toHexString(checksumInputStream(filename));
        end = System.currentTimeMillis();
        System.out.println("CRC32 value (file was read by FileInputStream): %s".formatted(crcValue));
        System.out.println("Time spent: %d millseconds".formatted(end - start));

        start = System.currentTimeMillis();
        crcValue = Long.toHexString(checksumBufferedInputStream(filename));
        end = System.currentTimeMillis();
        System.out.println("CRC32 value (file was read by BufferedInputStream): %s".formatted(crcValue));
        System.out.println("Time spent: %d millseconds".formatted(end - start));
        
        start = System.currentTimeMillis();
        crcValue = Long.toHexString(checksumRandomAccessFile(filename));
        end = System.currentTimeMillis();
        System.out.println("CRC32 value (file was read by RandomAccessFile): %s".formatted(crcValue));
        System.out.println("Time spent: %d millseconds".formatted(end - start));

        start = System.currentTimeMillis();
        crcValue = Long.toHexString(checksumMappedFile(filename));
        end = System.currentTimeMillis();
        System.out.println("CRC32 value (file was read by MappedByteBuffer): %s".formatted(crcValue));
        System.out.println("Time spent: %d millseconds".formatted(end - start));
    }

    public static long checksumInputStream(Path filename) throws IOException {
        try (InputStream in =  Files.newInputStream(filename)) {
            CRC32 crc = new CRC32();
            boolean done = false;
            while (!done) {
                int c = in.read();
                if (c == -1) done = true;
                else crc.update(c);
            }
            return crc.getValue();
        }
    }

    public static long checksumBufferedInputStream(Path filename) throws IOException {
        try (BufferedInputStream buffin = new BufferedInputStream(Files.newInputStream(filename))) {
            CRC32 crc = new CRC32();
            boolean done = false;
            while (!done) {
                int c = buffin.read();
                if (c == -1) done = true;
                else crc.update(c);
            }
            return crc.getValue();
        }
    }

    public static long checksumRandomAccessFile(Path filename) throws IOException {
        try (RandomAccessFile randomin = new RandomAccessFile(filename.toFile(), "r")) {
            long length = randomin.length();
            CRC32 crc = new CRC32();
            for (long i = 0; i < length; i++) {
                int c = randomin.readByte();
                crc.update(c);
            }
            return crc.getValue();
        }
    }

    public static long checksumMappedFile(Path filename) throws IOException {
        try (FileChannel channelin = FileChannel.open(filename)) {
            CRC32 crc = new CRC32();
            int length = (int) channelin.size();
            MappedByteBuffer buffer = channelin.map(FileChannel.MapMode.READ_ONLY, 0, length);
            for (int i = 0; i < length; i++) {
                int c = buffer.get(i);
                crc.update(c);
            }
            return crc.getValue();
        }
    }
}
