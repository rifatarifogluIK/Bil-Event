package org.rusteze.bilevent;

import org.bson.Document;

import java.io.FileNotFoundException;

public interface ConvertibleWithDocument<T> {
    public Document toDocument();
    public T fromDocument(Document doc) throws FileNotFoundException;
}
