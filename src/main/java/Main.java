public class Main {
    public static void main(String[] args) {
        SimpleContainer container = new SimpleContainer();
        container.registerType(A.class, B.class, true);
        A firstB = (A)container.resolve(A.class);
        A secondB = (A)container.resolve(A.class);
        System.out.println(firstB);
        System.out.println(secondB);
    }


}
