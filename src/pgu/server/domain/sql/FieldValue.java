package pgu.server.domain.sql;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class FieldValue {

    @Id
    private Long   id;

    private String field;
    private String value;
    private int    counter = 1;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (field == null ? 0 : field.hashCode());
        result = prime * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FieldValue other = (FieldValue) obj;
        if (field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!field.equals(other.field)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FieldValue [id=" + id + ", field=" + field + ", value=" + value + ", counter=" + counter + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(final int counter) {
        this.counter = counter;
    }

}
