import java.io.*;
import java.nio.file.Files;

public class FileWorker {

    private String path;
    private RandomAccessFile file;

    public FileWorker(String path) {
        this.path = path; // initialized path
    }

    public long goTo(int num) throws IOException { // transition to the specified symbol
        file = new RandomAccessFile(path, "r");
        file.seek(num);
        long pointer = file.getFilePointer(); // get cursor in file
        file.close();
        return pointer;
    }

    public static void copy(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public static void delete(String nameFile) throws FileNotFoundException {
        exists(nameFile);
        new File(nameFile).delete();
    }

    public static void update(String nameFile, String newText) throws FileNotFoundException {
        exists(nameFile);
        StringBuilder sb = new StringBuilder();
        String oldFile = read(nameFile);
        sb.append(oldFile);
        sb.append(newText);
        write(nameFile, sb.toString());
    }

    public static void write(String fileName, String text) {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                out.print(text);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String read() throws IOException { // read file and displays contents
        file = new RandomAccessFile(path, "r");
        String res = "";
        int b = file.read();
        while(b != -1){
            res = res + (char)b;
            b = file.read();
        }
        file.close();
        return res;
    }

    public String readFrom(int numberSymbol) throws IOException { // read file with a particular symbol
        file = new RandomAccessFile(path, "r");
        String res = "";
        file.seek(numberSymbol);
        int b = file.read();
        while(b != -1){
            res = res + (char)b;
            b = file.read();
        }
        file.close();
        return res;
    }

    public void write(String st) throws IOException { // write file
        file = new RandomAccessFile(path, "rw"); // modifier rw (read & write)
        file.write(st.getBytes());
        file.close();
    }

    public void removeLineFromFile(String file, String lineToRemove) {
        try {
            File inFile = new File(file);
            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            File tempFile = new File(inFile.getAbsolutePath() + ".tmp"); // construct the new file that will later be renamed to the original filename
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line;
            //Read from the original file and write to the new
            //unless content matches data to be removed
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            // rename the new file to the filename the original file had
            if (!tempFile.renameTo(inFile)) {
                System.out.println("Could not rename file");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        assert file.exists() : file.getName();
    }

    public static String read(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        StringBuilder sb = new StringBuilder();
        exists(fileName);
        try (BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}