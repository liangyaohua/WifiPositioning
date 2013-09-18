package project;

public class Pair<T1, T2>
{
  public Pair(T1 f, T2 s)
  { 
    first = f;
    second = s;   
  }

  public String toString()
  { 
    return "(" + first.toString() + ", " + second.toString() + ")"; 
  }

  public T1 first;
  public T2 second;
}