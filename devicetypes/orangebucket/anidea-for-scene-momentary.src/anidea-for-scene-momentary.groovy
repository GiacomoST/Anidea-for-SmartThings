/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * --------------------------------------------------------------------------------- *
 * Anidea for Scene Momentary
 * ==========================
 * Version:	 20.12.23.00
 *
 * This device handler implements a Momentary button that activates a scene via the
 * API, saving the need to assemble HTTPS POST requests in third party apps.
 */

include 'asynchttp_v1'

metadata
{
	definition( name: 'Anidea for Scene Momentary', namespace: 'orangebucket', author: 'Graham Johnson' )
    {
    	//
        capability 'Momentary'
		// 
		capability 'Health Check'
        //
        capability 'Actuator'
	}

	preferences
	{
   		input name: 'token', type: 'text', title: 'Personal Access Token', description: 'UUID', required: true
    	input name: 'scene', type: 'text', title: 'Scene ID',              description: 'UUID', required: true
	}
}

// installed() is called when the device is created, and when the device is updated in the IDE.
def installed()
{	
	logger( 'installed', 'info', '' )

    // Health Check is undocumented but this seems to be the common way of creating an untracked
    // device that will appear online when the hub is up.
	sendEvent( name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false )
}

// updated() seems to be called after installed() when the device is first installed, but not when
// it is updated in the IDE without there having been any actual changes.  It runs whenever settings
// are updated in the mobile app. It often used to be seen running twice in quick succession so was
// debounced in many handlers.
def updated()
{
	logger( 'updated', 'info', '' )
}

def logger( method, level = 'debug', message = '' )
{
	// Using log."${level}" for dynamic method invocation is now deprecated.
    switch( level )
	{
		case 'info':	log.info  "$device.displayName [$device.name] [${method}] ${message}"
        				break
        default:	    log.debug "$device.displayName [$device.name] [${method}] ${message}"
        				break
	}
}

// push() is the command for the Momentary capability. Make it press the button once.
def push()
{
	logger( 'push', 'info', '' )
    
    if ( token && scene )
    {
    	def params = [
        				uri:     'https://api.smartthings.com',
        			    path:    "/v1/scenes/${scene}/execute",
                        headers: [ Authorization: "Bearer $token" ]
                     ]
    
    	asynchttp_v1.post( response, params )
    }
    else
    {
    	logger( 'push', 'debug', 'Personal Access Token and Scene ID required.' )
    }
}

// parse() is called when the hub receives a message from a device.
def parse( String description )
{
    logger( 'parse', 'debug', description )
    
	// Nothing should appear.
}

def response( response, data )
{
    logger( 'response', 'info', response.status )
    
    switch ( response.status ) {
    	case 200:
        	logger( 'response', 'debug', 'A status of 200 suggests the Scene should have been executed' )
            break
        case 400:
            logger( 'response', 'debug', 'A status of 400 suggests an invalid Scene ID' )
            break
        case 401:
            logger( 'response', 'debug', 'A status of 401 suggests an invalid Personal Access Token' )
            break
    }
}