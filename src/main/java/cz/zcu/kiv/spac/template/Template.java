package cz.zcu.kiv.spac.template;

import cz.zcu.kiv.spac.data.Constants;
import cz.zcu.kiv.spac.data.antipattern.Antipattern;
import cz.zcu.kiv.spac.data.antipattern.heading.AntipatternHeading;
import cz.zcu.kiv.spac.data.antipattern.heading.AntipatternTableHeading;
import cz.zcu.kiv.spac.data.antipattern.heading.AntipatternTextHeading;
import cz.zcu.kiv.spac.enums.TemplateFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class representing template.
 */
public class Template {

    private List<TemplateField> fieldList;

    /**
     * Constructor.
     * @param fieldList - List of template fields.
     */
    public Template(List<TemplateField> fieldList) {

        this.fieldList = fieldList;
    }

    public List<TemplateField> getFieldList() {

        return fieldList;
    }

    /**
     * Get list of field names (related, sources, ...).
     * @return List of field names.
     */
    public List<String> getFieldNameList() {

        List<String> fieldNameList = new ArrayList<>();

        for (TemplateField field : fieldList) {

            fieldNameList.add(field.getName());
        }

        return fieldNameList;
    }

    /**
     * Get list of field texts (Related Anti-patterns, Sources, ...).
     * @return list of field texts.
     */
    public List<String> getFieldTextList() {

        List<String> fieldNameList = new ArrayList<>();

        for (TemplateField field : fieldList) {

            fieldNameList.add(field.getText());
        }

        return fieldNameList;
    }

    /**
     * Get field by field name.
     * @param fieldName - Field name.
     * @return Field.
     */
    public TemplateField getField(String fieldName) {

        for (TemplateField field : fieldList) {

            if (field.getName().equals(fieldName)) {

                return field;
            }
        }
        return null;
    }

    /**
     * Compare antipatterns headings to template fields and return list of differences.
     * @param antipattern - Compared antipattern.
     * @return List of differences.
     */
    public List<String> getHeadingDifferences(Antipattern antipattern) {

        List<String> headingDifferences = new ArrayList<>();

        // Get antipattern headings texts.
        List<String> antipatternHeadingsTexts = antipattern.getAntipatternHeadingsTexts();

        // Check if there are any headings in antipattern.
        boolean headingsPresented = false;

        if (antipatternHeadingsTexts != null && antipatternHeadingsTexts.size() > 0) {

            headingsPresented = true;
        }

        // Iterate through every template field.
        for (TemplateField templateField : fieldList) {

            String text = "Missing heading '" + templateField.getText() + "' !";

            // Check only if template field is required.
            if (templateField.isRequired()) {

                // If antipattern contains any headings.
                if (headingsPresented) {

                    // If list of antipattern heading texts dont containt template field, then add difference.
                    if (!antipatternHeadingsTexts.contains(templateField.getText())) {

                        headingDifferences.add(text);

                    } else {

                        AntipatternHeading heading = antipattern.getAntipatternHeading(templateField.getText(), true);

                        TemplateFieldType templateFieldType = templateField.getType();

                        if (!templateFieldType.toString().equals(heading.getType().toString())) {

                            if (templateFieldType == TemplateFieldType.TABLE) {

                                headingDifferences.add("Heading '" + templateField.getText() + "' does not contains any record in table !");
                                antipatternHeadingsTexts.remove(templateField.getText());
                                continue;
                            }
                        }

                        switch (heading.getType()) {

                            case TABLE:

                                AntipatternTableHeading tableHeading = (AntipatternTableHeading) heading;

                                if (tableHeading.getRelations() == null) {

                                    if (tableHeading.getRelations().size() == 0) {

                                        headingDifferences.add("Heading '" + templateField.getText() + "' does not contains any record in table !");
                                    }
                                }
                                // Check if table contains all required columns.
                                TableField tableField = (TableField) templateField;

                                List<String> headingTableColumns = new ArrayList<>(tableHeading.getColumns());

                                for (TableColumnField tableColumnField : tableField.getColumns()) {

                                    if (!headingTableColumns.contains(tableColumnField.getText())) {

                                        headingDifferences.add("Heading '" + templateField.getText() + "': Table does not contains column '" + tableColumnField.getText() + "'");

                                    } else {

                                        headingTableColumns.remove(tableColumnField.getText());
                                    }
                                }

                                // If there are any extended table column, add it too.
                                for (String tableColumn : headingTableColumns) {

                                    headingDifferences.add("Heading '" + templateField.getText() + "': Column '" + tableColumn + "' is not presented in template!");
                                }

                                break;

                            case TEXT:

                                AntipatternTextHeading textHeading = (AntipatternTextHeading) heading;

                                if (textHeading.getValue() == null || textHeading.getValue().length() == 0) {

                                    headingDifferences.add("Heading '" + templateField.getText() + "' does not contains any text !");
                                }
                                break;
                        }

                        // If contains, then remove it from list.
                        antipatternHeadingsTexts.remove(templateField.getText());
                    }

                    continue;
                }

                // If headings are not presented, just add heading text to list.
                headingDifferences.add(text);

            } else {

                // Make sure that even small "optional" is included in field name.
                antipatternHeadingsTexts.remove(templateField.getText() + Constants.TEMPLATE_FIELD_OPTIONAL_STRING);
                antipatternHeadingsTexts.remove(templateField.getText() + Constants.TEMPLATE_FIELD_OPTIONAL_STRING.toLowerCase());
            }
        }

        // If there are any extended headings in antipattern, then add them too.
        for (String remainingHeading : antipatternHeadingsTexts) {

            headingDifferences.add("Heading '" + remainingHeading + "' is not presented in template !");
        }

        return headingDifferences;
    }
}
