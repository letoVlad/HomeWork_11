public class Book {
    private int publicateYear;
    private Author author;

    public Book(int publicationYear, Author author) {
        this.publicateYear = publicationYear;
        this.author = author;
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

    public boolean info() {
        System.out.println(getAuthorName() + " " + getAuthorLastName() + " " + getPublicateYear());
        return false;
    }
}
