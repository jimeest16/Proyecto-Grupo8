package ucr.lab.TDA;


    public class LinkedStack implements Stack {
        private Node top;// es un apuntador
        private int counter; //cont elementos apilados

        public LinkedStack(){
            this.top = null;
            this.counter = 0;
        }


        @Override
        public int size() {
            return counter;
        }

        @Override
        public void clear() {
            top = null;
            counter = 0;
        }

        @Override
        public boolean isEmpty() {
            return top==null;
        }

        @Override
        public Object peek() throws StackException {
            if(isEmpty())
                throw new StackException("Linked Stack is empty");
            return top.data;
        }

        @Override
        public Object top() throws StackException {
            if(isEmpty())
                throw new StackException("Linked Stack is empty");
            return top.data;
        }

        @Override
        public void push(Object element) throws StackException {
            Node newNode = new Node(element);
            if(!isEmpty())
                newNode.next = top;
            top = newNode; //le decimos a tope que apunte a newNode
            this.counter++; //incremento el contador
        }

        @Override
        public Object pop() throws StackException {
            if(isEmpty())
                throw new StackException("Linked Stack is empty");
            Object topData = top.data;
            top = top.next; //movemos top al sgte nodo
            counter--;
            return topData;
        }

        @Override
        public String toString() {
            if(isEmpty()) return "Linked Stack is Empty";
            String result="Linked Stack Content:\n";
            try{
                LinkedStack aux = new LinkedStack();
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
