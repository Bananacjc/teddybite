import React, { useEffect, useState } from 'react';
import {
  Box, Heading, Button,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  FormControl, FormLabel, Input, Select, useDisclosure, useToast,
  FormErrorMessage, InputGroup, InputRightElement, IconButton, Checkbox,
  Table, Thead, Tbody, Tr, Th, Td, Badge, HStack, SimpleGrid, Icon,
  AlertDialog, AlertDialogBody, AlertDialogFooter, AlertDialogHeader, AlertDialogContent, AlertDialogOverlay, Text
} from '@chakra-ui/react';
import { AddIcon, ViewIcon, ViewOffIcon, EditIcon, DeleteIcon, SearchIcon, TriangleDownIcon, TriangleUpIcon, CloseIcon } from '@chakra-ui/icons';
import { getAllEmployees, createEmployee, updateEmployee, deleteEmployee, deleteEmployees, getEmployeePositions } from '../../api/employees';

const EmployeeManagement = () => {
  const [employees, setEmployees] = useState([]);
  const [positions, setPositions] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  const [errors, setErrors] = useState({});

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Search and Sort State
  const [filters, setFilters] = useState({
    name: '',
    position: '',
    salary: '',
    email: '',
    contactNo: '',
    dob: '',
    dateJoined: ''
  });
  const [activeSearches, setActiveSearches] = useState({}); // { name: true, email: false }
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });

  // Delete Dialog State
  const { isOpen: isDeleteOpen, onOpen: onDeleteOpen, onClose: onDeleteClose } = useDisclosure();
  const [deleteType, setDeleteType] = useState(null); // 'single' or 'batch'
  const [deleteId, setDeleteId] = useState(null);
  const cancelRef = React.useRef();

  // Selection State
  const [selectedIds, setSelectedIds] = useState([]);

  // Form State
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    contactNo: '',
    gender: '',
    position: '',
    salary: 0,
    dob: '',
    password: '',
    confirmPassword: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [empData, posData] = await Promise.all([
        getAllEmployees(),
        getEmployeePositions()
      ]);
      setEmployees(empData);
      setPositions(posData);
    } catch (error) {
      toast({
        title: "Error fetching data",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setFormData(prev => {
      const newData = { ...prev, [name]: value };

      // Auto-update salary when position changes
      if (name === 'position') {
        if (value && positions[value]) {
          newData.salary = positions[value];
        } else {
          newData.salary = 0;
        }
      }

      return newData;
    });

    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleOpenAdd = () => {
    setIsEditMode(false);
    setEditingId(null);
    setFormData({
      name: '',
      email: '',
      contactNo: '',
      gender: '',
      position: '',
      salary: 0,
      dob: '',
      password: '',
      confirmPassword: ''
    });
    setErrors({});
    onOpen();
  };

  const handleEdit = (emp) => {
    setIsEditMode(true);
    setEditingId(emp.employeeID);

    // Format DOB for input (YYYY-MM-DD)
    let formattedDob = '';
    if (emp.dob) {
      formattedDob = new Date(emp.dob).toISOString().split('T')[0];
    }

    setFormData({
      name: emp.name,
      email: emp.email,
      contactNo: emp.contactNo,
      gender: emp.gender,
      position: emp.position,
      salary: emp.salary || (positions[emp.position] || 0),
      dob: formattedDob,
      password: '', // Not used in edit
      confirmPassword: ''
    });
    setErrors({});
    onOpen();
  };

  const handleSubmit = async () => {
    const newErrors = {};

    // 1. Validate Required Fields
    if (!formData.name) newErrors.name = "Name is required";
    if (!formData.email) newErrors.email = "Email is required";
    if (!formData.contactNo) newErrors.contactNo = "Contact No is required";
    if (!formData.gender) newErrors.gender = "Gender is required";
    if (!formData.position) newErrors.position = "Position is required";
    if (!formData.dob) newErrors.dob = "Date of Birth is required";

    // Validate Password only in Add Mode
    if (!isEditMode) {
      if (!formData.password) newErrors.password = "Password is required";
      if (!formData.confirmPassword) newErrors.confirmPassword = "Confirm Password is required";
      if (formData.password !== formData.confirmPassword) {
        newErrors.confirmPassword = "Passwords do not match";
      }
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});

    try {
      const payload = {
        name: formData.name,
        email: formData.email,
        contactNo: formData.contactNo,
        gender: formData.gender,
        position: formData.position,
        dob: formData.dob ? `${formData.dob}T00:00:00` : null,
      };

      if (isEditMode) {
        // Update existing employee (no password)
        await updateEmployee(editingId, payload);
        toast({ title: "Employee updated", status: "success", duration: 3000 });
      } else {
        // Create new employee (with password)
        payload.password = formData.password;
        await createEmployee(payload);
        toast({ title: "Employee created", status: "success", duration: 3000 });
      }

      onClose();
      // Refresh employees but keep positions
      const empData = await getAllEmployees();
      setEmployees(empData);

    } catch (error) {
      console.log("FULL ERROR RESPONSE:", error.response);
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
      } else {
        toast({
          title: isEditMode ? "Failed to update" : "Failed to create",
          description: "Something went wrong",
          status: "error",
          duration: 3000,
        });
      }
    }
  };

  const handleDelete = (id) => {
    setDeleteType('single');
    setDeleteId(id);
    onDeleteOpen();
  };

  const handleBatchDelete = () => {
    setDeleteType('batch');
    onDeleteOpen();
  };

  const confirmDelete = async () => {
    onDeleteClose();
    try {
      if (deleteType === 'single') {
        await deleteEmployee(deleteId);
        toast({ title: "Employee deleted", status: "success" });
      } else if (deleteType === 'batch') {
        await deleteEmployees(selectedIds);
        toast({ title: "Employees deleted", status: "success" });
        setSelectedIds([]);
      }
      // Refresh employees
      const empData = await getAllEmployees();
      setEmployees(empData);
    } catch (error) {
      toast({ title: "Delete failed", status: "error" });
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      const allIds = filteredAndSortedEmployees.map(emp => emp.employeeID);
      setSelectedIds(allIds);
    } else {
      setSelectedIds([]);
    }
  };

  const handleSelectRow = (id) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(sid => sid !== id));
    } else {
      setSelectedIds([...selectedIds, id]);
    }
  };

  // Sorting Logic
  const handleSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const toggleSearch = (columnKey, isOpen) => {
    setActiveSearches(prev => ({ ...prev, [columnKey]: isOpen }));
    if (!isOpen) {
      // Clear filter when closing search
      handleFilterChange(columnKey, '');
    }
  };

  const filteredAndSortedEmployees = React.useMemo(() => {
    let processedItems = [...employees];

    // Apply Filters
    Object.keys(filters).forEach(key => {
      const filterValue = filters[key].toLowerCase();
      if (filterValue) {
        processedItems = processedItems.filter(emp => {
          let cellValue = emp[key];

          // Handle different data types for filtering
          if (key === 'salary' && cellValue !== undefined) {
            return cellValue.toString().includes(filterValue);
          }

          // For dates, check against the displayed formatted string
          if ((key === 'dob' || key === 'dateJoined') && cellValue) {
            const formattedDate = new Date(cellValue).toLocaleDateString('en-GB');
            return formattedDate.includes(filterValue);
          }

          if (cellValue) {
            return cellValue.toString().toLowerCase().includes(filterValue);
          }
          return false;
        });
      }
    });

    // Sort items
    if (sortConfig.key !== null) {
      processedItems.sort((a, b) => {
        let valA = a[sortConfig.key];
        let valB = b[sortConfig.key];

        // Safe check for null values
        if (valA === null || valA === undefined) valA = '';
        if (valB === null || valB === undefined) valB = '';

        // String comparison case-insensitive
        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (valA > valB) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    return processedItems;
  }, [employees, sortConfig, filters]);

  // Helper to render sort icon
  const getSortIcon = (columnName) => {
    if (sortConfig.key !== columnName) {
      // Show faded icon to indicate sortability
      return <TriangleDownIcon ml={1} w={3} h={3} color="gray.300" />;
    }
    return sortConfig.direction === 'ascending' ?
      <TriangleUpIcon ml={1} w={3} h={3} color="brand.600" /> :
      <TriangleDownIcon ml={1} w={3} h={3} color="brand.600" />;
  };

  // Helper for Header Cell with Search
  const HeaderCell = ({ label, columnKey, width }) => {
    const isSearchOpen = activeSearches[columnKey];

    return (
      <Th width={width} verticalAlign="top" py={2}>
        <Box>
          <HStack justify="space-between" width="100%" mb={isSearchOpen ? 2 : 0}>
            <Box
              cursor="pointer"
              onClick={() => handleSort(columnKey)}
              display="flex"
              alignItems="center"
              _hover={{ color: "brand.600" }}
              flex={1}
              title="Click to sort"
            >
              {label} {getSortIcon(columnKey)}
            </Box>
            <IconButton
              aria-label="Search"
              icon={isSearchOpen ? <CloseIcon /> : <SearchIcon />}
              size="xs"
              variant="ghost"
              color={isSearchOpen ? "red.400" : "gray.400"}
              _hover={{ color: isSearchOpen ? "red.500" : "brand.500" }}
              onClick={() => toggleSearch(columnKey, !isSearchOpen)}
            />
          </HStack>

          {isSearchOpen && (
            <Input
              size="sm"
              autoFocus
              placeholder={`Search...`}
              value={filters[columnKey] || ''}
              onChange={(e) => handleFilterChange(columnKey, e.target.value)}
              bg="white"
              borderColor="gray.200"
              _focus={{ borderColor: "brand.500", boxShadow: "none" }}
            />
          )}
        </Box>
      </Th>
    );
  };

  return (
    <Box>
      <HStack justify="space-between" mb={6}>
        <Heading size="lg" color="brown.900">Employee Management</Heading>
        <HStack>
          {selectedIds.length > 0 && (
            <Button leftIcon={<DeleteIcon />} colorScheme="red" variant="outline" onClick={handleBatchDelete}>
              Delete Selected ({selectedIds.length})
            </Button>
          )}
          <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleOpenAdd}>
            Add Employee
          </Button>
        </HStack>
      </HStack>

      <Box bg="white" borderRadius="xl" boxShadow="sm" p={4}>
        <Box overflowX="auto">
          <Table variant="simple">
            <Thead>
              <Tr>
                <Th width="40px" px={2}>
                  <Checkbox
                    isChecked={selectedIds.length === filteredAndSortedEmployees.length && filteredAndSortedEmployees.length > 0}
                    isIndeterminate={selectedIds.length > 0 && selectedIds.length < filteredAndSortedEmployees.length}
                    onChange={handleSelectAll}
                    colorScheme="brand"
                  />
                </Th>
                <HeaderCell label="ID" columnKey="employeeID" width="120px" />
                <HeaderCell label="Name" columnKey="name" width="200px" />
                <HeaderCell label="Position" columnKey="position" width="180px" />
                <HeaderCell label="Salary" columnKey="salary" width="150px" />
                <HeaderCell label="Email" columnKey="email" width="220px" />
                <HeaderCell label="Contact" columnKey="contactNo" width="150px" />
                <HeaderCell label="DOB" columnKey="dob" width="150px" />
                <HeaderCell label="Joined" columnKey="dateJoined" width="150px" />
                <Th pt={3} width="100px">Action</Th>
              </Tr>
            </Thead>
            <Tbody>
              {filteredAndSortedEmployees.map((emp) => (
                <Tr key={emp.employeeID || emp.id}>
                  <Td px={2}>
                    <Checkbox
                      isChecked={selectedIds.includes(emp.employeeID)}
                      onChange={() => handleSelectRow(emp.employeeID)}
                      colorScheme="brand"
                    />
                  </Td>
                  <Td fontSize="xs" color="gray.500">{emp.employeeID}</Td>
                  <Td fontWeight="bold">{emp.name}</Td>
                  <Td>{emp.position}</Td>
                  <Td>RM {emp.salary?.toFixed(2)}</Td>
                  <Td>{emp.email}</Td>
                  <Td>{emp.contactNo}</Td>
                  <Td>{emp.dob ? new Date(emp.dob).toLocaleDateString('en-GB') : 'N/A'}</Td>
                  <Td>{emp.dateJoined ? new Date(emp.dateJoined).toLocaleDateString('en-GB') : 'N/A'}</Td>
                  <Td>
                    <HStack spacing={2}>
                      <Button size="sm" colorScheme="blue" variant="ghost" onClick={() => handleEdit(emp)}>
                        <EditIcon />
                      </Button>
                      <Button size="sm" colorScheme="red" variant="ghost" onClick={() => handleDelete(emp.employeeID)}>
                        <DeleteIcon />
                      </Button>
                    </HStack>
                  </Td>
                </Tr>
              ))}
              {filteredAndSortedEmployees.length === 0 && !isLoading && (
                <Tr>
                  <Td colSpan={8} textAlign="center" py={4}>No employees found matching your filters.</Td>
                </Tr>
              )}
            </Tbody>
          </Table>
        </Box>
      </Box>

      {/* Add/Edit Employee Modal */}
      <Modal isOpen={isOpen} onClose={onClose} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>{isEditMode ? "Edit Employee" : "Add New Employee"}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <SimpleGrid columns={2} spacing={4}>
              {/* NAME FIELD */}
              <FormControl isRequired isInvalid={!!errors.name}>
                <FormLabel>Full Name</FormLabel>
                <Input name="name" value={formData.name} onChange={handleInputChange} />
                <FormErrorMessage>{errors.name}</FormErrorMessage>
              </FormControl>

              {/* EMAIL FIELD */}
              <FormControl isRequired isInvalid={!!errors.email}>
                <FormLabel>Email</FormLabel>
                <Input name="email" type="email" value={formData.email} onChange={handleInputChange} />
                <FormErrorMessage>{errors.email}</FormErrorMessage>
              </FormControl>

              {/* PASSWORD FIELDS (Only show in Add Mode) */}
              {!isEditMode && (
                <>
                  <FormControl isRequired isInvalid={!!errors.password}>
                    <FormLabel>Password</FormLabel>
                    <InputGroup>
                      <Input
                        name="password"
                        type={showPassword ? "text" : "password"}
                        placeholder="Min 6 chars"
                        value={formData.password}
                        onChange={handleInputChange}
                      />
                      <InputRightElement>
                        <IconButton
                          variant="ghost"
                          icon={showPassword ? <ViewOffIcon /> : <ViewIcon />}
                          onClick={() => setShowPassword(!showPassword)}
                          aria-label={showPassword ? "Hide password" : "Show password"}
                          size="sm"
                        />
                      </InputRightElement>
                    </InputGroup>
                    <FormErrorMessage>{errors.password}</FormErrorMessage>
                  </FormControl>

                  <FormControl isRequired isInvalid={!!errors.confirmPassword}>
                    <FormLabel>Confirm Password</FormLabel>
                    <InputGroup>
                      <Input
                        name='confirmPassword'
                        type={showConfirmPassword ? "text" : "password"}
                        placeholder='Re-enter password'
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                      />
                      <InputRightElement>
                        <IconButton
                          variant="ghost"
                          icon={showConfirmPassword ? <ViewOffIcon /> : <ViewIcon />}
                          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                          aria-label={showConfirmPassword ? "Hide password" : "Show password"}
                          size="sm"
                        />
                      </InputRightElement>
                    </InputGroup>

                    <FormErrorMessage>{errors.confirmPassword}</FormErrorMessage>
                  </FormControl>
                </>
              )}

              {/* CONTACT NUMBER FIELD */}
              <FormControl isRequired isInvalid={!!errors.contactNo}>
                <FormLabel>Contact No</FormLabel>
                <Input name="contactNo" value={formData.contactNo} onChange={handleInputChange} />
                <FormErrorMessage>{errors.contactNo}</FormErrorMessage>
              </FormControl>

              {/* GENDER FIELD */}
              <FormControl isRequired isInvalid={!!errors.gender}>
                <FormLabel>Gender</FormLabel>
                <Select placeholder='Select Gender' name="gender" value={formData.gender} onChange={handleInputChange}>
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </Select>
                <FormErrorMessage>{errors.gender}</FormErrorMessage>
              </FormControl>

              {/* POSITION FIELD */}
              <FormControl isRequired isInvalid={!!errors.position}>
                <FormLabel>Position</FormLabel>
                <Select placeholder='Select Position' name="position" value={formData.position} onChange={handleInputChange}>
                  {Object.entries(positions)
                    .sort(([, salaryA], [, salaryB]) => salaryB - salaryA)
                    .map(([pos, salary]) => (
                      <option key={pos} value={pos}>
                        {pos.replace('_', ' ')} (RM {salary})
                      </option>
                    ))}
                </Select>
                <FormErrorMessage>{errors.position}</FormErrorMessage>
              </FormControl>

              <FormControl isRequired isInvalid={!!errors.dob}>
                <FormLabel>Date of Birth</FormLabel>
                <Input name="dob" type="date" value={formData.dob} onChange={handleInputChange} max={new Date().toISOString().split('T')[0]} />
                <FormErrorMessage>{errors.dob}</FormErrorMessage>
              </FormControl>

            </SimpleGrid>
          </ModalBody>

          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose}>Cancel</Button>
            <Button colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleSubmit}>
              {isEditMode ? "Update Employee" : "Save Employee"}
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <AlertDialog
        isOpen={isDeleteOpen}
        leastDestructiveRef={cancelRef}
        onClose={onDeleteClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Employee{deleteType === 'batch' ? 's' : ''}
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure? You can't undo this action afterwards.
              {deleteType === 'batch' && (
                <Text mt={2} fontWeight="bold">
                  You are about to delete {selectedIds.length} employees.
                </Text>
              )}
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onDeleteClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={confirmDelete} ml={3}>
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>

    </Box >
  );
};

export default EmployeeManagement;
