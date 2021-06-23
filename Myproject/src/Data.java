class people{
    Integer age;
    String name;
    public people(int a, String n){
        age = a;
        name = n;
    }

    public String toString(){
        String s = new String();
        s = age.toString() + "  "  + name;
        return s;
    }
}

