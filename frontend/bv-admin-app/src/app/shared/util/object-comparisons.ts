export function sortedStringify(obj: any) {
    const allKeys: any[] = [];
    JSON.stringify(obj, (key, value) => {
        allKeys.push(key);
        return value;
    });
    allKeys.sort();
    return JSON.stringify(obj, allKeys);
}

const obj1 = {
    uuid: '1234',
    vorname: 'John',
    nachname: 'Doe',
    email: 'john.doe@example.com',
    sortByLableName: null,
};

const obj2 = {
    vorname: 'John',
    nachname: 'Doe',
    email: 'john.doe@example.com',
    sortByLableName: null,
    uuid: '1234',
};

const areEqual = sortedStringify(obj1) === sortedStringify(obj2);
console.log('Are objects equal?', areEqual); // Output: true
