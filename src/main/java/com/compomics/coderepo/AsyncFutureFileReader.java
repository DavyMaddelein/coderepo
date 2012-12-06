/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.*;

/**
 *
 * @author Davy
 */

 
public class AsyncFutureFileReader {
 
  public static void main(String[] args) {
 
    //Buffer to read the contents from the file.
    ByteBuffer buffer = ByteBuffer.allocate(100);
 
    //The file to read the contents from.
    Path path = Paths.get("/home/tests/test.txt");
 
    //Creating the asynchronous channel to the file which allows reading and writing of content.
    try(AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(path)){
 
      //Returns a Future instance which can be used to read the contents of the file.
      Future<Integer> fileResult = asyncChannel.read(buffer, 0);
 
      //Waiting for the file reading to complete. Another way to do this is using completionHandler
      while(!fileResult.isDone()){
        System.out.println("Waiting to complete the file reading ...");
      }
 
      //Print the number of bytes read.
      System.out.println("Number of bytes read: "+fileResult.get());
 
      //Reset the current position of the buffer to the beginning and the limit to the current position.
      buffer.flip();
 
      //Decode the contents of the byte buffer.
      System.out.println("Contents of file: ");
      System.out.println(Charset.defaultCharset().decode(buffer));
 
    }catch(IOException | InterruptedException | ExecutionException ex){
      ex.printStackTrace();
    }
  }
}