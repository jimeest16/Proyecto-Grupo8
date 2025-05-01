package ucr.lab.domain;

public class ArrayStack implements Stack{

    private int n; //el tam max de la pila
    private int top; //para llevar el control del tope de la pila
    private Object dataStack[];


    //Constructor
    public ArrayStack(int n){
        if(n<=0) System.exit(1); //se sale
        this.n = n;
        this.top = -1; //indica q la pila esta vacia
        this.dataStack = new Object[n];
    }
    @Override
    public int size() {
        return top+1;
    }

    @Override
    public void clear() {
        this.top = -1; //indica q la pila esta vacia
        this.dataStack = new Object[n];
    }

    @Override
    public boolean isEmpty() {
        return top==-1;
    }

    @Override
    public Object peek() throws StackException {
        if(isEmpty())
            throw new StackException("Array Stack is empty");
        return this.dataStack[top];
    }

    @Override
    public Object top() throws StackException {
        if(isEmpty())
            throw new StackException("Array Stack is empty");
        return this.dataStack[top];
    }

    @Override
    public void push(Object element) throws StackException {
        if(top==dataStack.length-1)
            throw new StackException("Array Stack is full");
        dataStack[++top] = element;
    }

    @Override
    public Object pop() throws StackException {
        if(isEmpty())
            throw new StackException("Array Stack is empty");
        return dataStack[top--];
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Array Stack is Empty";
        String result="Array Stack Content:\n";
        try{
            ArrayStack aux = new ArrayStack(size());
            while(!isEmpty()){
                result+=peek()+"\n";
                aux.push(pop());
            }
            //ahora debemos dejar la pila en su estado original
            while(!aux.isEmpty()){
                push(aux.pop());
            }

        }catch(StackException ex){
            System.out.println(ex.getMessage());
        }
        return result;
    }
}