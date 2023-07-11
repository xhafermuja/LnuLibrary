package com.example.lnulibrary;

public class Book {
    private int ISBN;
    private String title;
    private int num_of_copies;
    private String author;
    private int year;

    public Book(){};

    public Book(int ISBN, String title, int num_of_copies, String author, int year){
        this.ISBN= ISBN;
        this.title = title;
        this.num_of_copies= num_of_copies;
        this.author = author;
        this.year = year;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setBookTitle(String title) {
        this.title = title;
    }

    public int getNum_of_copies() {
        return num_of_copies;
    }

    public void setNumOfCopies(int num_of_copies) {
        this.num_of_copies = num_of_copies;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

