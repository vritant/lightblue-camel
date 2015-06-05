package com.redhat.lightblue.camel.dataformat;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonXmlDataFormat implements DataFormat {

    private final XmlMapper mapper = new XmlMapper();
    private final Class<?> type;

    public JacksonXmlDataFormat() {
        type = Object.class;
    }

    public JacksonXmlDataFormat(Class<?> type) {
        this.type = type;
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        return mapper.readValue(stream, type);
    }

}
