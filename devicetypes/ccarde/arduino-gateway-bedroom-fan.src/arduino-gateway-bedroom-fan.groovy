metadata {
	// Automatically generated. Make future change here.
	definition (name: "Arduino Gateway: Bedroom Fan", namespace: "ccarde", author: "Chris Carde") {
		capability "Switch Level"
		capability "Actuator"
		capability "Indicator"
		capability "Switch"
		//capability "Polling"
		//capability "Refresh"
		//capability "Sensor"
        //capability "speed"
        
        command "lowSpeed"
        command "medSpeed"
        command "highSpeed"

		attribute "currentSpeed", "string"

	}

     tiles (scale:2) {
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        	tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "onHigh", label:'High', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#407020", nextState: "turningOff"
            	attributeState "onMed", label:'Med', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#a0d080", nextState: "turningOff"
            	attributeState "onLow", label:'Low', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#ccffcc", nextState: "turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState: "turningOn"
				attributeState "turningOn", label:'Turning On...', icon:"st.switches.switch.on", backgroundColor:"#79b821"
				attributeState "turningOff", label:'Turning Off...', icon:"st.switches.switch.off", backgroundColor:"#ffffff"
			}
        	
            tileAttribute ("device.level", key: "VALUE_CONTROL") {
				attributeState "level", action:"switch level.setLevel"
			}
        }
        

		controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 6, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
        valueTile("currentSpeed", "device.currentSpeed", canChangeIcon: false, inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state ("default", label:'${currentValue}')
        }

//Speed control row
        standardTile("lowSpeed", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "lowSpeed", label:'LOW', action:"lowSpeed", icon:"st.Home.home30"
        }
        standardTile("medSpeed", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "medSpeed", label:'MED', action:"medSpeed", icon:"st.Home.home30"
        }
        standardTile("highSpeed", "device.level", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "highSpeed", label:'HIGH', action:"highSpeed", icon:"st.Home.home30"
        }

		main(["switch"])
		details(["switch", "lowSpeed", "medSpeed", "highSpeed", "currentSpeed", "levelSliderControl"])
	}
}

def parse(String description) {
        log.debug("parse called for " + description);
}


def on() {
    log.debug("fan on")
    if (state.lastSpeed == "low") {
      lowSpeed()
    }
    if (state.lastSpeed == "med") {
      medSpeed()
    }
    if (state.lastSpeed == "high") { 
      highSpeed()
    }
}

def off() {
    log.debug("fan off")
    state
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/arduino/fan/off",
        headers: [
            HOST: "10.9.5.40:80"
        ]
    )
    sendEvent(name: "currentSpeed", value: "OFF")
    sendEvent(name: "level", value: "0")
    sendEvent(name: "switch", value: "off")
    result
}

def setLevel(value) {
    def level = Math.min(value as Integer, 99)
    log.trace "setLevel(value): ${value}"
    if (value < 30 || value == 61)
    {
    	lowSpeed()
    }
   	else if (value < 62 || value == 98)
    {
    	medSpeed()
    }
    else
    {
    	highSpeed()
    }
	
}

def setLevel(value, duration) {
    setLevel(value)
}

def lowSpeed() {
    log.debug "Low speed settings"
    state.lastSpeed = "low"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/arduino/fan/low",
        headers: [
            HOST: "10.9.5.40:80"
        ]
    )
    sendEvent(name: "currentSpeed", value: "LOW")
    sendEvent(name: "level", value: "33")
    sendEvent(name: "switch", value: "onLow")
    result
}

def medSpeed() {
    log.debug "Medium speed settings"
    state.lastSpeed = "med"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/arduino/fan/medium",
        headers: [
            HOST: "10.9.5.40:80"
        ]
    )
    sendEvent(name: "currentSpeed", value: "MED")
    sendEvent(name: "level", value: "66")
    sendEvent(name: "switch", value: "onMed")
    result
}

def highSpeed() {
    log.debug "High speed settings"
    state.lastSpeed = "high"
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/arduino/fan/high",
        headers: [
            HOST: "10.9.5.40:80"
        ]
    )
    sendEvent(name: "currentSpeed", value: "ON")
    sendEvent(name: "level", value: "100")
    sendEvent(name: "switch", value: "onHigh")
    result
}


/*
def poll() {
	zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def refresh() {
	zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def indicatorWhenOn() {
	sendEvent(name: "indicatorStatus", value: "when on", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1).format()
}

def indicatorWhenOff() {
	sendEvent(name: "indicatorStatus", value: "when off", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()
}

def indicatorNever() {
	sendEvent(name: "indicatorStatus", value: "never", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1).format()
}

def invertSwitch(invert=true) {
	if (invert) {
		zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 4, size: 1).format()
	}
	else {
		zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 4, size: 1).format()
	}
}
*/