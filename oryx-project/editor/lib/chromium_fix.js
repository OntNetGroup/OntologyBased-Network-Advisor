/* 
 *  Javascript (good) hack fixing Chrome and Chromium bug that prevent using insertAdjacentHTML with namespaces
 *  Copyright (C) 2011  Florent FAYOLLE
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// check if Chrome/Chromium is used
if(/Chrome/.test(navigator.userAgent)){
		// save the native function of insertAdjacentHTML
		var proxy_insertAdjacentHTML = HTMLElement.prototype.insertAdjacentHTML;
		// function that replace all modified attributes to their real name
		function __clean_attr(node){
			var name;
			for(var i = 0; i < node.attributes.length; i++){
				name = node.attributes[i].nodeName;
				if( node.attributes[i].nodeName.indexOf("__colon__") >= 0){
					node.setAttribute(name.replace(/__colon__/g, ":"), node.getAttribute(name));
					node.removeAttribute(name);
				}
			}
		}
		// the new function insertAdjacentHTML will replace all attributes of that form : namespace:attribute="value"
		// to that form : namespace__colon__attribute="value"
		HTMLElement.prototype.insertAdjacentHTML = function(where, html){
			var new_html = html.replace( /([\S]+):([\S]+)=/g ,"$1__colon__$2=");
			// we call the native insertAdjacentHTML that will parse the HTML string to DOM
			proxy_insertAdjacentHTML.call(this, where, new_html);
			var nodes = this.getElementsByTagName("*");
			for(var i = 0; i < nodes.length; i++)
				__clean_attr(nodes[i]);
		}
}