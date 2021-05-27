package resolveTestClasses;

public class Bar1 {
    @DependecyConstructor
    public Bar1(Bar2 bar2, Bar3 bar3)
    {

    }

    public Bar1(Bar2 bar2, Bar3 bar3, Bar1 bar1) {}


}
