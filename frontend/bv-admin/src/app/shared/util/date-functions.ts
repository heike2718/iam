export function parseGermanDate(dateString: string): Date {
    const [day, month, year] = dateString.split('.').map(Number);
    return new Date(year, month - 1, day); // month is 0-based in JS Date
}


export function parseGermanDateTime(dateTimeString: string): Date {
    
    const [datePart, timePart] = dateTimeString.split(' ');
  
    const [day, month, year] = datePart.split('.').map(Number);
  
    const [hours, minutes, seconds] = timePart ? timePart.split(':').map(Number) : [0, 0, 0];
  
    return new Date(year, month - 1, day, hours, minutes, seconds);
  }
  