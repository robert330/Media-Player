package com.example.mediaplayer.id3v1tag;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class ID3v1  {
	
	

	private static RandomAccessFile randomAccessFile;
	static long length;

	ID3v1(){}
	

	    public static ID3 getMusicInfoV1(String path) {
	        if (path == null) {
	            return null;
	        }
	        ID3 id3;
	        try {
	            randomAccessFile = new RandomAccessFile(path, "r");
	            
	            byte[] buffer = new byte[128];
	            length=randomAccessFile.length();
	            
	            System.out.println("Size:" + length);
	            
	            randomAccessFile.seek(randomAccessFile.length() - 128);
	            randomAccessFile.read(buffer);
	            if (buffer.length == 128) {
	                id3 = new ID3();
	                String tag = new String(buffer, 0, 3);
	                
	                

	                if (tag.equalsIgnoreCase("TAG")) {

	                    String songName = new String(buffer, 3, 30).trim();
	                    String artist = new String(buffer, 33, 30).trim();
	                    String album = new String(buffer, 63, 30).trim();
	                    String year = new String(buffer, 93, 4).trim();
	                    String comment = new String(buffer, 97, 30).trim();
	                    String genre = new String(buffer, 127, 1).trim();
	                   
	                    int foo;
	                    try {
	                       foo = Integer.parseInt(genre);
	                    }
	                    catch (NumberFormatException e)
	                    {
	                       foo = -1;
	                    }
	                    id3.setLengthOfFile(length);
	                    id3.setTitle(songName);
	                    id3.setArtist(artist);
	                    id3.setAlbum(album);
	                    id3.setYear(year);
	                    id3.setComment(comment);
	                    id3.setGenre(foo);
	                    String genre1=id3.getGenreDescription();
	                    System.out.println("Song name:" + songName);
	                    System.out.println("Artist:" + artist);
	                    System.out.println("Subordinate record:" + album);
	                    System.out.println("Year of issue:" + year);
	                    System.out.println("Remarks:" + comment);
	                    
	                   
	                    if(foo!=-1) {
	                    	 System.out.println("Genre is null");
	                    }
	                    else {
	                    	 System.out.println("Genre:" + genre1);
	                    	
	                    }
	                    
	                   
	                    return id3;
	                } else {
	                	System.out.println("Invalid song information...");
	                    return null;
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
	        return null;
	    }


	
	    public static void setMusicInfoV1(String path, ID3 id3) {
	        try {
	            byte[] bufferAll = new byte[128];
	            byte[] buffTag;
	            byte[] buffSoundName = new byte[30];
	            byte[] buffArtist = new byte[30];
	            byte[] buffAlbum = new byte[30];
	            byte[] buffYear = new byte[4];
	            byte[] buffComment = new byte[30];
	            byte[] buffGenre = new byte[1];
	         

	            buffTag = "TAG".getBytes();
	            byte[] cache;
	            
	            if (id3.getGenre() != 0) {
	                cache = intToByteArray(id3.getGenre());
	                System.arraycopy(cache, 0, buffSoundName, 0, cache.length);
	            }

	            if (id3.getArtist() != null) {
	                cache = id3.getArtist().getBytes(StandardCharsets.UTF_8);
	                System.arraycopy(cache, 0, buffArtist, 0, cache.length);
	            }

	            if (id3.getAlbum() != null) {
					cache = id3.getAlbum().getBytes(StandardCharsets.UTF_8);
					System.arraycopy(cache, 0, buffAlbum, 0, cache.length);
				}

	            if (id3.getYear() != null) {
	                cache = id3.getYear().getBytes(StandardCharsets.UTF_8);
	                System.arraycopy(cache, 0, buffYear, 0, cache.length);
	            }

	            if (id3.getComment() != null) {
	                cache = id3.getComment().getBytes(StandardCharsets.UTF_8);
	                int num = 30;
	                if (cache.length <= num) {
	                    num = cache.length;
	                }
	                System.arraycopy(cache, 0, buffComment, 0, num);
	            }
	            if (id3.getTitle() != null) {
	                cache = id3.getTitle().getBytes(StandardCharsets.UTF_8);
	                System.arraycopy(cache, 0, buffSoundName, 0, cache.length);
	            }
	           

	            System.arraycopy(buffTag, 0, bufferAll, 0, 3);
	            System.arraycopy(buffSoundName, 0, bufferAll, 3, 30);
	            System.arraycopy(buffArtist, 0, bufferAll, 33, 30);
	            System.arraycopy(buffAlbum, 0, bufferAll, 63, 30);
	            System.arraycopy(buffYear, 0, bufferAll, 93, 4);
	            System.arraycopy(buffComment, 0, bufferAll, 97, 30);
	            System.arraycopy(buffGenre, 0, bufferAll, 127, 1);
	            
	            


	            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(path), "rw");

	            long len = randomAccessFile.length();
	            if (getMusicInfoV1(path) != null) {
	             
	                len = randomAccessFile.length() - 128;
	            }
	            randomAccessFile.seek(len);
	            randomAccessFile.write(bufferAll, 0, bufferAll.length);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	    public static byte[] intToByteArray(int value) {
	        return new byte[] {
	                (byte)(value >>> 24),
	                (byte)(value >>> 16),
	                (byte)(value >>> 8),
	                (byte)value};
	    }



}
