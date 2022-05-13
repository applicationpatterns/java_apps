# library compare 
this library module compares a list of nested objects

## Implemented Features
* Compare features
  * compare single node
    * node represents object or row
    * fields represent attributes or columns
    * fields are expected to key/value pairs. 
      * key is of string type representing attribute name or column name
      * value is of Object type. but actual supported types are
        * String
        * Long
        * Integer
        * List<ComparableNode>
        * ComparableNode
        * Map<String, String>: this represents custom fields of an object/row
  * compare list of nodes
  * compare node with nested/child list of nodes
  * compare node with a field whose value 


## Backlog
* compare node with nested/child node


## Pseudo code

```
ComparableNode
    // fields are a map of key value pairs
    // each key is a string (field name)
    // each value is either a string or ComparableNode
    Map<String, Object> fields;

    compareObject(this, base) {
        for each thisField in this.fields
            if thisField exists in base.fields 
                if thisField type != baseFieldType
                    // thisField type is different from the baseField
                else if thisField type == string
                    // both are strings, so lets compare values
                    if thisField value is same as baseField value
                        // all good 
                    else 
                        // thisField is different from baseField
                else if thisField type == ComparableNode
                    compareObject(thisField.value, baseField.value)
                else 
                    // unknown field type
                end if
            else
                // thisField is extra (compared to the base)
        end for
        for each baseField in base.fields
            if baseField does not exist in this.fields
                // this field is missing (compared to the base)
        end for
    }
    
    // assume list to be ordered the same way
    compareObjectInOrderedList(thisList, baseList) {
        for each thisObject in thisList
            if thisObject does not exist in baseList
                // thisObject is extra compared to the base
            else
                compareObject (thisObject, baseObject)
            end if 
        end for
        for each baseObject in baseList
            if baseObject does not exist in thisList
                // thisObject is missing compared to the base
            end if 
        end for
    }

```
## References

* for reading from JSON or writing to JSON [baeldung json tutorial](https://www.baeldung.com/jackson-object-mapper-tutorial)

1234587600000 = 2009-02-14 00:00:00

