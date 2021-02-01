/*
 * ---------------------------------------------------------------------------------
 * (C) Graham Johnson (orangebucket)
 *
 * SPDX-License-Identifier: MIT
 * ---------------------------------------------------------------------------------
 *
 * Anidea for Virtual Presence
 * ===========================
 * Version:	 20.12.23.00
 *
 * A virtual presence and occupancy sensor that handles the two capabilities separately. 
 * Custom commands use standard names where available.
 */

metadata 
{
    definition ( name: 'Anidea for Virtual Presence', namespace: 'orangebucket', author: 'Graham Johnson' ) 
    {
    	// The DTH can work as both a Presence Sensor and an Occupancy Sensor.  The two
        // capabilities are not linked within the handler.
        capability 'Presence Sensor'
        capability 'Occupancy Sensor'
        
        capability 'Sensor'
        capability 'Health Check'

		// Custom commands for setting the presence, named for compatibility with
        // the stock Simulated Presence Sensor DTH as a de facto standard.
        capability 'circlemusic21301.presenceCommands'

		// Custom commands for setting occupancy.  No known standards to follow.
		capability 'circlemusic21301.occupancyCommands'
    }
}

def installed()
{
    logger( 'installed', 'info', '' )
    
    // Set up Health Check using the untracked enrollment method, copied from stock handlers.
    sendEvent( name: 'DeviceWatch-Enroll', value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false ) 

	// Initialise the attributes to keep the SmartThings app happy. Set the defaults
    // to the values most likely to keep things secure.
    sendEvent( name: 'presence',  value: 'not present', displayed: false )
    sendEvent( name: 'occupancy', value: 'unoccupied',  displayed: false )
}

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
// This should be redundant.
def parse( description )
{
    logger( 'parse', 'info', description )
}

def arrived() 
{
    logger( 'arrived', 'info', '' )
    
    sendEvent( name: 'presence', value: 'present' )
}


def departed() 
{
    logger( 'departed', 'info', '' )
    
    sendEvent( name: 'presence', value: 'not present' )
}

def occupied() 
{
    logger( 'occupied', 'info', '' )
    
    sendEvent( name: 'occupancy', value: 'occupied' )
}

def unoccupied() 
{
    logger( 'unoccupied', 'info', '' )
    
    sendEvent( name: 'occupancy', value: 'unoccupied' )
}