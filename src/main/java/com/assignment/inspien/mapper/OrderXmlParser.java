package com.assignment.inspien.mapper;

import com.assignment.inspien.dto.OrderGroupDto;
import com.assignment.inspien.dto.OrderHeaderDto;
import com.assignment.inspien.dto.OrderItemDto;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderXmlParser {

    public List<OrderGroupDto> parse(String rawXml) throws Exception {
        String wrapped = "<ROOT>" + rawXml + "</ROOT>";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(wrapped));

        List<OrderGroupDto> groups = new ArrayList<>();

        OrderHeaderDto currentHeader = null;
        List<OrderItemDto> currentItems = null;

        String currentTag = null;
        String userId = null, name = null, address = null, status = null;
        String itemId = null, itemName = null, price = null;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("HEADER")) {
                    if (currentHeader != null) {
                        groups.add(new OrderGroupDto(currentHeader, currentItems));
                    }
                    currentHeader = null;
                    currentItems = new ArrayList<>();
                    userId = name = address = status = null;
                } else if (tagName.equals("ITEM")) {
                    itemId = itemName = price = null;
                }
                currentTag = tagName;

            } else if (event == XMLStreamConstants.CHARACTERS) {
                String text = reader.getText().trim();
                if (text.isEmpty() || currentTag == null) {
                    continue;
                }
                switch (currentTag) {
                    case "USER_ID":
                        userId = text;
                        break;
                    case "NAME":
                        name = text;
                        break;
                    case "ADDRESS":
                        address = text;
                        break;
                    case "STATUS":
                        status = text;
                        break;
                    case "ITEM_ID":
                        itemId = text;
                        break;
                    case "ITEM_NAME":
                        itemName = text;
                        break;
                    case "PRICE":
                        price = text;
                        break;
                    default:
                        break;
                }

            } else if (event == XMLStreamConstants.END_ELEMENT) {
                String tagName = reader.getLocalName();

                if (tagName.equals("HEADER")) {
                    currentHeader = new OrderHeaderDto(userId, name, address, status);
                } else if (tagName.equals("ITEM")) {
                    currentItems.add(new OrderItemDto(userId, itemId, itemName, price));
                }
                currentTag = null;
            }
        }

        if (currentHeader != null) {
            groups.add(new OrderGroupDto(currentHeader, currentItems));
        }

        reader.close();
        return groups;
    }
}