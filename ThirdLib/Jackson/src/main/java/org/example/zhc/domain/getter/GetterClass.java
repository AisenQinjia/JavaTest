package org.example.zhc.domain.getter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetterClass extends AbstractGetterClass {
    int type;
    int level;
    boolean isEnabled;
    public GetterClass(){
    }
//    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
//    public GetterClass(@JsonProperty("type") int type,@JsonProperty("level") int level ){
//        this.level = level;
//    }
//    public GetterClass(String type){
//        this.type = Integer.parseInt(type);
//    }

    @Override
    public String getType(){
        return "fsd";
    }

}
