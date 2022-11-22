public class Main {

    public static void main(String[] args) {
        Book harryPotter = new Book("Harry Potter", 1995, new Author("Joanne ", "Rowling"));
        Book theLordOfTheRing = new Book("The Lord Of The Ring", 1955, new Author("John ", "Tolkien"));
        System.out.println(harryPotter.info());
        System.out.println(theLordOfTheRing.info());
        System.out.println(theLordOfTheRing.getPublicateYear());
    }
}