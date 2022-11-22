public class Book {
    private int publicateYear;
    private Author author;
    private String nameBook;

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
    }

    public Book(String nameBook, int publicationYear, Author author) {
        this.publicateYear = publicationYear;
        this.author = author;
        this.nameBook = nameBook;
    }

    public int getPublicateYear() {
        return publicateYear;
    }

    public void setPublicateYear(int publicateYear) {
        this.publicateYear = publicateYear;
    }

    public String getAuthorName() {
        return author.getName();
    }

    public String getAuthorLastName() {
        return author.getLastName();
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String info() {
        return (getAuthorName() + " " + getAuthorLastName() + " " + getPublicateYear()+ " " + getNameBook());

    }
}
