package com.zjlp.face.metaq.util;

import java.io.IOException;
import java.io.Serializable;

import com.taobao.metamorphosis.client.extension.spring.MessageBodyConverter;
import com.taobao.metamorphosis.exception.MetaClientException;
import com.taobao.metamorphosis.utils.codec.impl.JavaDeserializer;
import com.taobao.metamorphosis.utils.codec.impl.JavaSerializer;

public class MyJavaSerializationMessageBodyConverter  implements MessageBodyConverter<Serializable>{

	JavaSerializer serializer = new JavaSerializer();
    JavaDeserializer deserializer = new JavaDeserializer();
	@Override
	public byte[] toByteArray(Serializable body) throws MetaClientException {
		try {
            return this.serializer.encodeObject(body);
        }
        catch (IOException e) {
            throw new MetaClientException(e);

        }
	}

	@Override
	public Serializable fromByteArray(byte[] bs) throws MetaClientException {
		return (Serializable)bs;
	}

}
