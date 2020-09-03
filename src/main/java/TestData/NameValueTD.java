package TestData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NameValueTD  {

    private String name;
    private String value;
    private String regex;
    private boolean velocityExpression;
    private int regexGroup = 1;

    public NameValueTD() { }

    public NameValueTD(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public NameValueTD(String name, String value, String regex, int regexGroup, boolean velocityExpression) {
        this.name = name;
        this.value = value;
        this.regex = regex;
        this.regexGroup = regexGroup;
        this.velocityExpression = velocityExpression;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public boolean getVelocityExpression() { return velocityExpression; }
    public void setVelocityExpression(boolean velocityExpression) { this.velocityExpression = velocityExpression; }

    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }

    public int getRegexGroup() { return regexGroup; }
    public void setRegexGroup(int regexGroup) { this.regexGroup = regexGroup; }

    //region Overrides
    //Source: http://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof NameValueTD))
            return false;
        if(object == this)
            return true;

        NameValueTD testedData = (NameValueTD)object;

        return new EqualsBuilder().
                append(name, testedData.name).
                append(value, testedData.value).
                append(regex, testedData.regex).
                append(regexGroup, testedData.regexGroup).
                append(velocityExpression, testedData.velocityExpression).
                isEquals();
    }

    //Source: http://stackoverflow.com/questions/27581/what-issues-should-be-considered-when-overriding-equals-and-hashcode-in-java
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(name).
                append(value).
                append(regex).
                append(regexGroup).
                append(velocityExpression).
                toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Name: " + name + " Value: " + value + " - Velocity Expression: " + velocityExpression);
        if(regex != null && regex.length() > 0)
            sb.append(" - Regex Group: " + regexGroup + " Regex: " + regex);
        return sb.toString();
    }
    //endregion Overrides
}
