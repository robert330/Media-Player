package com.example.mediaplayer.id3v1tag;

public class ID3 {
    String title;
    String artist;
    String album;
    String year;
    String comment;
    int genre;
    long length;

    public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }
    public void setLengthOfFile(long length) {
    	this.length=length;
    }
    public long getLengthOfFile() {
        return length;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getGenreDescription() {
		try {
			return ID3v1Genres.GENRES[genre];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Unknown";
		}
	}

}
