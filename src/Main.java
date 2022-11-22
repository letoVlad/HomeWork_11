public class Main {

    public static void main(String[] args) {
        Book harryPotter = new Book(1995, new Author("Joanne ", "Rowling"));
        Book theLordOfTheRing = new Book(1955, new Author("John ", "Tolkien"));
        System.out.println(harryPotter.info());
        System.out.println(harryPotter.getAuthorName());
        System.out.println(harryPotter.getAuthorLastName());
        theLordOfTheRing.setPublicateYear(1988);
        System.out.println(theLordOfTheRing.getPublicateYear());
    }
}